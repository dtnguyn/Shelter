package com.nguyen.shelter.di

import androidx.paging.ExperimentalPagingApi
import com.google.firebase.auth.FirebaseAuth
import com.nguyen.shelter.api.mapper.PropertyDetailNetworkMapper
import com.nguyen.shelter.api.mapper.PropertyNetworkMapper
import com.nguyen.shelter.api.service.RealtorApiService
import com.nguyen.shelter.db.ShelterDatabase
import com.nguyen.shelter.db.mapper.PropertyCacheMapper
import com.nguyen.shelter.db.mapper.PropertyFilterCacheMapper
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
        propertyCacheMapper: PropertyCacheMapper,
        filterCacheMapper: PropertyFilterCacheMapper,
        auth: FirebaseAuth
    ): MainRepository{
        return MainRepository(
            service,
            database,
            propertyNetworkMapper,
            propertyDetailNetworkMapper,
            propertyCacheMapper,
            filterCacheMapper,
            auth
        )
    }
}