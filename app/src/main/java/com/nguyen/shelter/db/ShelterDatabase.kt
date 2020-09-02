package com.nguyen.shelter.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nguyen.shelter.db.entity.PropertyCacheDao
import com.nguyen.shelter.db.entity.PropertyCacheEntity
import com.nguyen.shelter.db.entity.RemoteKeysDao
import com.nguyen.shelter.db.entity.RemoteKeysEntity

@Database(
    entities = [PropertyCacheEntity::class, RemoteKeysEntity::class],
    version = 4,
    exportSchema = false
)
abstract class ShelterDatabase: RoomDatabase() {

    abstract fun propertyDao(): PropertyCacheDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object{
        val DATABASE_NAME: String = "shelter_db"
    }

}