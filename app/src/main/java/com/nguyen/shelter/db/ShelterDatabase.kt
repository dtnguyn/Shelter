package com.nguyen.shelter.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nguyen.shelter.db.entity.*

@Database(
    entities = [PropertyCacheEntity::class, RemoteKeysEntity::class, PropertyFilterCacheEntity::class, PropertyDetailCacheEntity::class],
    version = 6,
    exportSchema = false
)
abstract class ShelterDatabase: RoomDatabase() {

    abstract fun propertyDao(): PropertyCacheDao
    abstract fun remoteKeysDao(): RemoteKeysDao
    abstract fun filterDao(): PropertyFilterCacheDao
    abstract fun detailDao(): PropertyDetailCacheDao

    companion object{
        val DATABASE_NAME: String = "shelter_db"
    }

}