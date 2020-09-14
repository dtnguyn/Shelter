package com.nguyen.shelter.api.response

import com.google.gson.annotations.SerializedName

data class Phone(
    @SerializedName("number")
    var number: String,

    @SerializedName("type")
    var type: String,

    @SerializedName("primary")
    var primary: Boolean
)