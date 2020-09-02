package com.nguyen.shelter.repo

import android.app.Activity
import androidx.paging.*
import com.facebook.AccessToken
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.nguyen.shelter.api.mapper.PropertyDetailNetworkMapper
import com.nguyen.shelter.api.mapper.PropertyNetworkMapper
import com.nguyen.shelter.api.service.RealtorApiService
import com.nguyen.shelter.db.ShelterDatabase
import com.nguyen.shelter.db.entity.PropertyCacheEntity
import com.nguyen.shelter.db.mapper.PropertyCacheMapper
import com.nguyen.shelter.model.CallbackResponse
import com.nguyen.shelter.model.PropertyFilter
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainRepository
@ExperimentalPagingApi
@Inject
constructor(
    private val service: RealtorApiService,
    private val database: ShelterDatabase,
    private val propertyNetworkMapper: PropertyNetworkMapper,
    private val propertyDetailNetworkMapper: PropertyDetailNetworkMapper,
    private val propertyCacheMapper: PropertyCacheMapper,
    private val auth: FirebaseAuth
){

    companion object {
        private const val NETWORK_PAGE_SIZE = 200
    }

    @ExperimentalPagingApi
    fun getRentPropertyList(filter: PropertyFilter): Flow<PagingData<PropertyCacheEntity>>{
        val propertyDao = database.propertyDao()
        val pagingSourceFactory = {propertyDao.getProperties("for_rent")}
        val remoteMediator = RentRemoteMediator(
            propertyNetworkMapper,
            propertyCacheMapper,
            filter,
            service,
            database
        )

        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = remoteMediator,
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    @ExperimentalPagingApi
    fun getSalePropertyList(filter: PropertyFilter): Flow<PagingData<PropertyCacheEntity>>{
        val propertyDao = database.propertyDao()
        val pagingSourceFactory = {propertyDao.getProperties("for_sale")}
        val remoteMediator = SaleRemoteMediator(
            propertyNetworkMapper,
            propertyCacheMapper,
            filter,
            service,
            database
        )

        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = remoteMediator,
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }


    fun checkAuthentication(callback: (response: CallbackResponse<FirebaseUser>) -> Unit){
        if(auth.currentUser == null){
            callback.invoke(CallbackResponse(false, "User are not logged in.", auth.currentUser))
        } else {
            callback.invoke(CallbackResponse(true, "User are already logged in.", auth.currentUser))
        }

    }


    fun loginWithFacebook(token: AccessToken, activity: Activity, callback: (response: CallbackResponse<FirebaseUser>) -> Unit){
        println("debug: preparing authentication..")
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if(task.isSuccessful){
                    callback.invoke(CallbackResponse(true, "Log in successfully with Facebook.", auth.currentUser))
                } else {
                    callback.invoke(CallbackResponse(false, "Cannot log in with Facebook.", auth.currentUser))
                }

            }
    }

    fun loginWithGoogle(token: String, activity: Activity, callback: (response: CallbackResponse<FirebaseUser>) -> Unit){
        val credential = GoogleAuthProvider.getCredential(token, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if(task.isSuccessful){
                    callback.invoke(CallbackResponse(true, "Log in successfully with Google.", auth.currentUser))
                } else {
                    callback.invoke(CallbackResponse(false, "Cannot log in with Google.", auth.currentUser))
                }

            }
    }

}