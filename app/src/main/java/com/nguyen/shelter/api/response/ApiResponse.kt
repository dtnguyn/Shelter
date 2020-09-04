package com.nguyen.shelter.api.response

import com.google.gson.annotations.SerializedName
import com.nguyen.shelter.api.entity.PropertyNetworkEntity

data class ApiResponse<T> (
    @SerializedName("properties")
    var data: List<T>?
)