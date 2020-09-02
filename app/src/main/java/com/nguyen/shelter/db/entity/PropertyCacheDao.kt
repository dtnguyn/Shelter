package com.nguyen.shelter.db.entity

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PropertyCacheDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(property: PropertyCacheEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repos: List<PropertyCacheEntity>)


    @Query("SELECT * FROM properties WHERE prop_status = :query")
    fun getProperties(query: String): PagingSource<Int, PropertyCacheEntity>

    @Query("DELETE FROM properties WHERE prop_status")
    suspend fun clearProperties()



}