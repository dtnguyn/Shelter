package com.nguyen.shelter.api.entity

import com.google.gson.annotations.SerializedName
import com.nguyen.shelter.api.response.*

data class PropertyNetworkEntity(

    @SerializedName("property_id")
    var id: String?,

    @SerializedName("listing_id")
    var listingId: String?,

    @SerializedName("prop_type")
    var category: String?,

    @SerializedName("prop_status")
    var status: String?,

    @SerializedName("address")
    var address: Address?,

    @SerializedName("community")
    var features: Features?,

    @SerializedName("photo_count")
    var photoCount: Int?,

    @SerializedName("beds")
    var beds: Int?,

    @SerializedName("baths")
    var baths: Int?,

    @SerializedName("price")
    var price: Int?,

    @SerializedName("photos")
    var photos: List<Photo>?,

    @SerializedName("thumbnail")
    var thumbnail: String?,

    @SerializedName("building_size")
    var buildingSize: BuildingSize?

)