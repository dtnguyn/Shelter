package com.nguyen.shelter.api.response

import com.google.gson.annotations.SerializedName

data class Features (

    @SerializedName("id")
    var id: Int? = null,

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("baths_min")
    var bathsMin: Double? = null,

    @SerializedName("baths_max")
    var bathsMax: Double? = null,

    @SerializedName("beds_min")
    var bedsMin: Double? = null,

    @SerializedName("beds_max")
    var bedsMax: Double? = null,

    @SerializedName("price_min")
    var priceMin: Double? = null,

    @SerializedName("price_max")
    var priceMax: Double? = null,

    @SerializedName("sqft_min")
    var areaMin: Double? = null,

    @SerializedName("sqft_max")
    var areaMax: Double? = null
)