package com.nguyen.shelter.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nguyen.shelter.db.entity.*

@Database(
    entities = [PropertyCacheEntity::class, RemoteKeysEntity::class, PropertyFilterCacheEntity::class],
    version = 5,
    exportSchema = false
)
abstract class ShelterDatabase: RoomDatabase() {

    abstract fun propertyDao(): PropertyCacheDao
    abstract fun remoteKeysDao(): RemoteKeysDao
    abstract fun filterDao(): PropertyFilterCacheDao

    companion object{
        val DATABASE_NAME: String = "shelter_db"
    }

}