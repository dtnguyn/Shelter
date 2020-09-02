package com.nguyen.shelter.api.response

import com.google.gson.annotations.SerializedName

data class SchoolLocation(

    @SerializedName("street")
    var street: String?,

    @SerializedName("city")
    var city: String?,

    @SerializedName("country")
    var country: String?,

    @SerializedName("state")
    var state: String?,

    @SerializedName("postal_code")
    var postalCode: String?
)
