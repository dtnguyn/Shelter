package com.nguyen.shelter.db.entity

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PropertyDetailCacheDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(property: PropertyDetailCacheEntity): Long

    @Query("SELECT * FROM properties_detail WHERE property_id = :id")
    fun getPropertyDetail(id: String): PropertyDetailCacheEntity?

    @Query("DELETE FROM properties_detail")
    suspend fun clearAll()

}