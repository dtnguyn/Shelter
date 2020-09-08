package com.nguyen.shelter.db.entity

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nguyen.shelter.model.PropertyFilter

@Dao
interface PropertyFilterCacheDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilter(filter: PropertyFilterCacheEntity): Long

    @Query("SELECT * FROM property_filters WHERE filter_type = :filterType")
    fun getPropertyFilter(filterType: String): PropertyFilterCacheEntity?


}