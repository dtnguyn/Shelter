package com.nguyen.shelter.di

import androidx.paging.ExperimentalPagingApi
import com.google.firebase.auth.FirebaseAuth
import com.nguyen.shelter.api.mapper.PropertyDetailNetworkMapper
import com.nguyen.shelter.api.mapper.PropertyNetworkMapper
import com.nguyen.shelter.api.service.RealtorApiService
import com.nguyen.shelter.db.ShelterDatabase
import com.nguyen.shelter.db.mapper.PropertyCacheMapper
import com.nguyen.shelter.repo.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object FirebaseModule {

    @ExperimentalPagingApi
    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth{
        return FirebaseAuth.getInstance()
    }
}