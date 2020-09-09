package com.nguyen.shelter.api.response

import com.google.gson.annotations.SerializedName

data class FloorPlan(

    @SerializedName("id")
    var id: Int?,

    @SerializedName("baths")
    var baths: Double?,

    @SerializedName("beds")
    var beds: Double?,

    @SerializedName("photo_count")
    var photoCount: Int?,

    @SerializedName("photo")
    var photo: Photo?,

    @SerializedName("price")
    var price: Int?,

    @SerializedName("sqft")
    var area: Int?
)