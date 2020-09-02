package com.nguyen.shelter.api.response

import com.google.gson.annotations.SerializedName

data class CommunityFeature(

    @SerializedName("category")
    var category: String?,

    @SerializedName("text")
    var featuresString: List<String>?
)