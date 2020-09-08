package com.nguyen.shelter.di

import android.content.Context
import androidx.room.Room
import com.nguyen.shelter.db.ShelterDatabase
import com.nguyen.shelter.db.ShelterDatabase.Companion.DATABASE_NAME
import com.nguyen.shelter.db.entity.PropertyCacheDao
import com.nguyen.shelter.db.entity.PropertyFilterCacheDao
import com.nguyen.shelter.db.entity.RemoteKeysDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object RoomModule {

    @Singleton
    @Provides
    fun provideShelterDb(@ApplicationContext context: Context): ShelterDatabase {
        return Room
            .databaseBuilder(
                context,
                ShelterDatabase::class.java,
                DATABASE_NAME
            )
            .fallbackToDestructiveMigration()
            .build()

    }

    @Singleton
    @Provides
    fun providePropertyDao(shelterDatabase: ShelterDatabase): PropertyCacheDao{
        return shelterDatabase.propertyDao()
    }

    @Singleton
    @Provides
    fun provideRemoteKeysDao(shelterDatabase: ShelterDatabase): RemoteKeysDao{
        return shelterDatabase.remoteKeysDao()
    }

    @Singleton
    @Provides
    fun provideFilterDao(shelterDatabase: ShelterDatabase): PropertyFilterCacheDao{
        return shelterDatabase.filterDao()
    }

}