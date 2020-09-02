package com.nguyen.shelter.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nguyen.shelter.api.response.Address
import com.nguyen.shelter.api.response.DataList
import com.nguyen.shelter.api.response.Features
import com.nguyen.shelter.api.response.Photo

@Entity(tableName = "properties")
class PropertyCacheEntity (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name= "property_id")
    var id: String,

    @ColumnInfo(name = "listing_id")
    var listingId: String?,

    @ColumnInfo(name = "prop_type")
    var category: String?,

    @ColumnInfo(name = "prop_status")
    var status: String?,

    @ColumnInfo(name = "address")
    var address: String,

    @ColumnInfo(name = "community")
    var features: String,

    @ColumnInfo(name = "photo_count")
    var photoCount: Int?,

    @ColumnInfo(name = "beds")
    var beds: Int?,

    @ColumnInfo(name = "baths")
    var baths: Int?,

    @ColumnInfo(name = "price")
    var price: Int?,

    @ColumnInfo(name = "photos")
    var photos: String?,

    @ColumnInfo(name = "thumbnail")
    var thumbnail: String?,

    @ColumnInfo(name = "building_size")
    var buildingSize: String?
)