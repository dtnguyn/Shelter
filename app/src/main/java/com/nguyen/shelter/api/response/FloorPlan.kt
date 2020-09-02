package com.nguyen.shelter.api.response

import com.google.gson.annotations.SerializedName

data class FloorPlan(

    @SerializedName("id")
    var id: Int?,

    @SerializedName("baths")
    var baths: Int?,

    @SerializedName("beds")
    var beds: Int?,

    @SerializedName("photo_count")
    var photoCount: Int?,

    @SerializedName("photo")
    var photo: Photo?,

    @SerializedName("price")
    var price: Int?,

    @SerializedName("sqft")
    var area: Int?
)