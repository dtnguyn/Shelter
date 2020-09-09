package com.nguyen.shelter.api.response

import com.google.gson.annotations.SerializedName

data class Office(

    @SerializedName("id")
    var id: String? = "",

    @SerializedName("name")
    var name: String? = "",

    @SerializedName("type")
    var type: String? = "",

    @SerializedName("phones")
    var phones: List<Phone>? = null,

    @SerializedName("hours")
    var workingHour: List<WorkingHour>? = null
)