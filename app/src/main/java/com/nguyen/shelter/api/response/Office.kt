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
    var phones: DataList<Phone>? = null,

    @SerializedName("hours")
    var workingHour: WorkingHour? = null
)