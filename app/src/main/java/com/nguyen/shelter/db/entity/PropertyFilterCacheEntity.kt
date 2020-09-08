package com.nguyen.shelter.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "property_filters")
data class PropertyFilterCacheEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name= "filter_type")
    var filterType: String,

    @ColumnInfo(name= "city")
    var city: String,

    @ColumnInfo(name= "state_code")
    var stateCode: String,

    @ColumnInfo(name= "postal_code")
    var postalCode: String?,

    @ColumnInfo(name= "age_min")
    var ageMin: Int?,

    @ColumnInfo(name= "age_max")
    var ageMax: Int?,

    @ColumnInfo(name= "beds_min")
    var bedsMin: Int?,

    @ColumnInfo(name= "beds_max")
    var bedsMax: Int?,

    @ColumnInfo(name= "baths_min")
    var bathsMin: Int?,

    @ColumnInfo(name= "baths_max")
    var bathsMax: Int?,

    @ColumnInfo(name= "price_min")
    var priceMin: Int?,

    @ColumnInfo(name= "price_max")
    var priceMax: Int?,

    @ColumnInfo(name= "lot_min")
    var lotMin: Int?,

    @ColumnInfo(name= "lot_max")
    var lotMax: Int?,

    @ColumnInfo(name= "area_max")
    var areaMax: Int?,

    @ColumnInfo(name= "area_min")
    var areaMin: Int?,

    @ColumnInfo(name= "has_open_houe")
    var hasOpenHouse: Boolean?,

    @ColumnInfo(name= "is_pending")
    var isPending: Boolean?,

    @ColumnInfo(name= "is_new_plan")
    var isNewPlan: Boolean?,

    @ColumnInfo(name= "is_contingent")
    var isContingent: Boolean?,

    @ColumnInfo(name= "is_new_construction")
    var isNewConstruction: Boolean?,

    @ColumnInfo(name= "is_foreclosure")
    var isForeclosure: Boolean?,

    @ColumnInfo(name= "type")
    var type: String?,

    @ColumnInfo(name= "sort")
    var sort: String?,

    @ColumnInfo(name= "allow_cats")
    var allow_cats: Boolean?,

    @ColumnInfo(name= "allow_dogs")
    var allow_dogs: Boolean?,

    @ColumnInfo(name= "features")
    var features: String?
)