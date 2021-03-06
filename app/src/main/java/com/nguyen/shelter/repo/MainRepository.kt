package com.nguyen.shelter.repo

import android.app.Activity
import androidx.paging.*
import com.facebook.AccessToken
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.gson.Gson
import com.nguyen.shelter.api.mapper.BlogFirebaseMapper
import com.nguyen.shelter.api.mapper.PropertyDetailNetworkMapper
import com.nguyen.shelter.api.mapper.PropertyNetworkMapper
import com.nguyen.shelter.api.service.RealtorApiService
import com.nguyen.shelter.db.ShelterDatabase
import com.nguyen.shelter.db.entity.PropertyCacheEntity
import com.nguyen.shelter.db.mapper.PropertyCacheMapper
import com.nguyen.shelter.db.mapper.PropertyDetailCacheMapper
import com.nguyen.shelter.db.mapper.PropertyFilterCacheMapper
import com.nguyen.shelter.model.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainRepository
@ExperimentalPagingApi
@Inject
constructor(
    private val service: RealtorApiService,
    private val database: ShelterDatabase,
    private val mapper: PropertyNetworkMapper,
    private val detailMapper: PropertyDetailNetworkMapper,
    private val detailCacheMapper: PropertyDetailCacheMapper,
    private val cacheMapper: PropertyCacheMapper,
    private val filterMapper: PropertyFilterCacheMapper,
    private val blogMapper: BlogFirebaseMapper,
    private val gson: Gson,
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
){

    private var rentFilter = PropertyFilter()
    private var saleFilter = PropertyFilter()

    companion object {
        private const val NETWORK_PAGE_SIZE = 30
        const val RENT = "rent_filter"
        const val SALE = "sale_filter"
    }

    @ExperimentalPagingApi
    fun getRentPropertyList(filter: PropertyFilter): Flow<PagingData<PropertyCacheEntity>>{
        val propertyDao = database.propertyDao()
        val pagingSourceFactory = {propertyDao.getProperties("for_rent")}
        val remoteMediator = RentRemoteMediator(
            mapper,
            cacheMapper,
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
            mapper,
            cacheMapper,
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


    suspend fun getPropertyDetail(id: String): PropertyDetail? {
        println("debug: Getting prop detail")
        var detail: PropertyDetail? = null
        val detailCacheEntity = database.detailDao().getPropertyDetail(id)

        println("debug: Check cache $detailCacheEntity")
        if(detailCacheEntity == null){
            val response = service.getPropertyDetail(id)
            val detailList = response.data
            println("debug: Check network ${detailList?.get(0)}")
            if(detailList != null && detailList.isNotEmpty()){
                detail = detailMapper.mapFromEntity(detailList[0])
                database.detailDao().insert(detailCacheMapper.mapToEntity(detail))
            }
        }  else {
            detail = detailCacheMapper.mapFromEntity(detailCacheEntity)
        }

        println("debug: Check final $detail")
        return detail

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

    fun logout(callback: (response: CallbackResponse<FirebaseUser>) -> Unit){
        auth.signOut()
        if(auth.currentUser == null){
            callback.invoke(CallbackResponse(true, "Log out successfully.", auth.currentUser))
        } else {
            callback.invoke(CallbackResponse(false, "Cannot log out.", auth.currentUser))
        }
    }

    suspend fun saveRentFilter(filter: PropertyFilter) {
        val cacheFilter = filterMapper.mapToEntity(filter)
        cacheFilter.filterType = RENT
        database.filterDao().insertFilter(cacheFilter)
    }

    suspend fun saveSaleFilter(filter: PropertyFilter) {
        val cacheFilter = filterMapper.mapToEntity(filter)
        cacheFilter.filterType = SALE
        database.filterDao().insertFilter(cacheFilter)
    }

    suspend fun getFilter(type: String): PropertyFilter{
        val cacheFilter = database.filterDao().getPropertyFilter(type) ?: return PropertyFilter()
        return filterMapper.mapFromEntity(cacheFilter)
    }

    fun updatePropertySaveStatus(savedList: HashMap<String, Property?>, property: Property, callback: (response: CallbackResponse<Unit>) -> Unit) {
        if(auth.currentUser == null) {
            callback.invoke(CallbackResponse(false, "User haven't logged in.", null))
            return
        }

        val savedMap = HashMap<String, String>()

        for ((key, value) in savedList) {
            savedMap[key] = gson.toJson(value)
        }

        println("debug: savedList: $savedList")
        db.collection("saves").document(auth.currentUser!!.uid)
            .set(savedMap)
            .addOnSuccessListener {
                callback.invoke(CallbackResponse(true, "Save/Unsave property successfully", null))
            }
            .addOnFailureListener {
                println("debug: report fail ${it.message}")
                callback.invoke(CallbackResponse(false, "Error when saving/Unsaving property: ${it.message}", null))
            }


    }

    fun getSavedProperties(callback: (response: CallbackResponse<HashMap<String, Property?>>) -> Unit){
        if(auth.currentUser == null) {
            callback.invoke(CallbackResponse(false, "User haven't logged in.", null))
            return
        }

        val docRef = db.collection("saves").document(auth.currentUser!!.uid)
        var map: HashMap<String, String>?

        docRef.get()
            .addOnSuccessListener {document ->
                map = if(document != null && document.data != null){
                    document.data!! as HashMap<String, String>
                } else {
                    HashMap()
                }
                println("Getting saved properties successfully")

                val propMap = HashMap<String, Property?>()
                for ((key, value) in map!!) {
                    propMap[key] = gson.fromJson(value, Property::class.java)
                }
                callback.invoke(CallbackResponse(true, "Getting saved properties successfully", propMap))
            }
            .addOnFailureListener {e ->
                println(e.message)
                callback.invoke(CallbackResponse(false, "Error when getting saved properties: ${e.message}", null))
            }

    }



}