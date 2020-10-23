package com.nguyen.shelter.api.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Address (
    @SerializedName("city")
    var city: String? = null,

    @SerializedName("country")
    var country: String? = null,

    @SerializedName("county")
    var county: String? = null,

    @SerializedName("lat")
    var latitude: Float? = null,

    @SerializedName("lon")
    var longitude: Float? = null,

    @SerializedName("line")
    var addressLine: String? = null,

    @SerializedName("postal_code")
    var postalCode: String? = null,

    @SerializedName("state_code")
    var stateCode: String? = null,

    @SerializedName("state")
    var state: String? = null,

    @SerializedName("time_zone")
    var timeZone: String? = null,

    @SerializedName("neighborhood_name")
    var neighborhoodName: String? = null
//    @SerializedName("neighborhoods")
//    var neighborhoods: DataList<Neighborhood>?
) : Parcelable