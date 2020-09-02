package com.nguyen.shelter.api.response

import com.google.gson.annotations.SerializedName

data class School(

    @SerializedName("id")
    var id: Long?,

    @SerializedName("nces_id")
    var secondId: String?,

    @SerializedName("name")
    var name: String?,

    @SerializedName("education_levels")
    var educationLevels: List<String>,

    @SerializedName("lat")
    var latitude: Float?,

    @SerializedName("lng")
    var longitude: Float?,

    @SerializedName("funding_type")
    var type: String?,

    @SerializedName("phone")
    var phone: String?,

    @SerializedName("distance_in_miles")
    var distance: Double?,

    @SerializedName("ratings")
    var ratings: SchoolRating?,

    @SerializedName("location")
    var location: SchoolLocation?

)