package com.nguyen.shelter.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nguyen.shelter.api.response.*

@Entity(tableName = "properties_detail")
class PropertyDetailCacheEntity (

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name= "property_id")
    var id: String,

    @ColumnInfo(name= "listing_id")
    var listingId: String?,

    @ColumnInfo(name= "category")
    var category: String?,

    @ColumnInfo(name= "year_built")
    var yearBuilt: String?,

    @ColumnInfo(name= "description")
    var description: String?,

    @ColumnInfo(name= "status")
    var status: String?,

    @ColumnInfo(name= "address")
    var address: String?,

    @ColumnInfo(name= "features")
    var features: String?,

    @ColumnInfo(name= "lot_size")
    var lotSize: Int?,

    @ColumnInfo(name= "url")
    var url: String?,

    @ColumnInfo(name= "community")
    var community: String?,

    @ColumnInfo(name= "floor_plan")
    var floorPlans: String?,

    @ColumnInfo(name= "lease_term")
    var leaseTerm: String?,

    @ColumnInfo(name= "photo_count")
    var photoCount: Int?,

    @ColumnInfo(name= "photos")
    var photos: String?,

    @ColumnInfo(name= "office")
    var office: String?,

    @ColumnInfo(name= "beds")
    var beds: Int?,

    @ColumnInfo(name= "baths")
    var baths: Int?,

    @ColumnInfo(name= "schools")
    var schools: String?,

    @ColumnInfo(name= "building_size")
    var size: String?,

    @ColumnInfo(name= "price")
    var price: String?
)