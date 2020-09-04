package com.nguyen.shelter.api.entity

import com.google.gson.annotations.SerializedName
import com.nguyen.shelter.api.response.*

data class PropertyDetailNetworkEntity(

    @SerializedName("property_id")
    var id: String?,

    @SerializedName("listing_id")
    var listingId: String?,

    @SerializedName("prop_type")
    var category: String?,

    @SerializedName("prop_status")
    var status: String?,

    @SerializedName("address")
    var address: Address?,

    @SerializedName("community")
    var features: Features?,

    @SerializedName("photo_count")
    var photoCount: Int?,

    @SerializedName("beds")
    var beds: Int?,

    @SerializedName("baths")
    var baths: Int?,

    @SerializedName("photos")
    var photos: List<Photo>?,

    @SerializedName("year_built")
    var yearBuilt: Int?,

    @SerializedName("description")
    var description: String?,

    @SerializedName("lot_size")
    var lotSize: LotSize?,

    @SerializedName("rdc_web_url")
    var url: String?,

    @SerializedName("features")
    var community: List<CommunityFeature>?,

    @SerializedName("floor_plans")
    var floorPlans: List<FloorPlan>?,

    @SerializedName("lease_term")
    var leaseTerm: String?,

    @SerializedName("office")
    var office: Office?,

    @SerializedName("schools")
    var schools: List<School>?



)