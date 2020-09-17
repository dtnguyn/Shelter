package com.nguyen.shelter.api.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
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
) : Parcelable
