package com.nguyen.shelter.api.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Photo (

    @SerializedName("href")
    val url: String?


) : Parcelable