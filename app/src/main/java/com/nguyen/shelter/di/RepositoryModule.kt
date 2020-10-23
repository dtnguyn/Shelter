package com.nguyen.shelter.di

import androidx.paging.ExperimentalPagingApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import com.nguyen.shelter.api.mapper.BlogFirebaseMapper
import com.nguyen.shelter.api.mapper.CommentFirebaseMapper
import com.nguyen.shelter.api.mapper.PropertyDetailNetworkMapper
import com.nguyen.shelter.api.mapper.PropertyNetworkMapper
import com.nguyen.shelter.api.service.RealtorApiService
import com.nguyen.shelter.db.ShelterDatabase
import com.nguyen.shelter.db.mapper.PropertyCacheMapper
import com.nguyen.shelter.db.mapper.PropertyDetailCacheMapper
import com.nguyen.shelter.db.mapper.PropertyFilterCacheMapper
import com.nguyen.shelter.model.Blog
import com.nguyen.shelter.repo.BlogRepository
import com.nguyen.shelter.repo.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RepositoryModule {

    @ExperimentalPagingApi
    @Singleton
    @Provides
    fun provideMainRepository(
        service: RealtorApiService,
        database: ShelterDatabase,
        propertyNetworkMapper: PropertyNetworkMapper,
        propertyDetailNetworkMapper: PropertyDetailNetworkMapper,
        propertyDetailCacheMapper: PropertyDetailCacheMapper,
        propertyCacheMapper: PropertyCacheMapper,
        filterCacheMapper: PropertyFilterCacheMapper,
        blogFirebaseMapper: BlogFirebaseMapper,
        auth: FirebaseAuth,
        gson: Gson,
        fireStore: FirebaseFirestore,
    ): MainRepository{
        return MainRepository(
            service,
            database,
            propertyNetworkMapper,
            propertyDetailNetworkMapper,
            propertyDetailCacheMapper,
            propertyCacheMapper,
            filterCacheMapper,
            blogFirebaseMapper,
            gson,
            auth,
            fireStore
        )
    }

    @Singleton
    @Provides
    fun provideBlogRepository(
        commentFirebaseMapper: CommentFirebaseMapper,
        blogFirebaseMapper: BlogFirebaseMapper,
        fireStore: FirebaseFirestore,
        storageReference: StorageReference,
        auth: FirebaseAuth
    ): BlogRepository{
        return BlogRepository(blogFirebaseMapper, commentFirebaseMapper, fireStore, storageReference, auth)
    }

}