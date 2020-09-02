package com.nguyen.shelter.api.response

import com.google.gson.annotations.SerializedName

data class Photo (

    @SerializedName("href")
    val url: String?

)