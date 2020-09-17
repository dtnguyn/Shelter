package com.nguyen.shelter.api.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SchoolRating (

    @SerializedName("great_schools_rating")
    var schoolRating: Int?,

    @SerializedName("parent_rating")
    var parentRating: Int
) : Parcelable
