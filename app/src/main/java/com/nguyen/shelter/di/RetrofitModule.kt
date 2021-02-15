package com.nguyen.shelter.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.nguyen.shelter.api.service.FirebaseApiService
import com.nguyen.shelter.api.service.RealtorApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RetrofitModule {

    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson{
        return GsonBuilder().create()
    }

    @Realtor
    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl("https://realtor.p.rapidapi.com/properties/v2/")
            .addConverterFactory(GsonConverterFactory.create(gson))
    }


    @Singleton
    @Provides
    fun provideRealtorService(@Realtor retrofit: Retrofit.Builder): RealtorApiService {
        return retrofit
            .build()
            .create(RealtorApiService::class.java)
    }

    @FbMessaging
    @Singleton
    @Provides
    fun provideFirebaseRetrofit(gson: Gson): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl("https://fcm.googleapis.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
    }


    @Singleton
    @Provides
    fun provideFirebaseService(@FbMessaging retrofit: Retrofit.Builder): FirebaseApiService {
        return retrofit
            .build()
            .create(FirebaseApiService::class.java)
    }
}


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Realtor

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FbMessaging