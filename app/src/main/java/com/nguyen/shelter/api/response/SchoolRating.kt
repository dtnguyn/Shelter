package com.nguyen.shelter.api.response

import com.google.gson.annotations.SerializedName

data class SchoolRating (

    @SerializedName("great_schools_rating")
    var schoolRating: Int?,

    @SerializedName("parent_rating")
    var parentRating: Int
)
