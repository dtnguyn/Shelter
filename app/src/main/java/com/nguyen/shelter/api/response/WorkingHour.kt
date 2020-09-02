package com.nguyen.shelter.api.response

import com.google.gson.annotations.SerializedName

data class WorkingHour(

    @SerializedName("day")
    var workDays: List<String>?,

    @SerializedName("start_time")
    var startWorkingHour: String?,

    @SerializedName("end_time")
    var endWorkingHour: String?
)