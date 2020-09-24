package com.nguyen.shelter.model

import com.nguyen.shelter.api.response.*


data class PropertyDetail(
    var id: String,
    var listingId: String,
    var category: String,
    var yearBuilt: String,
    var description: String,
    var status: String,
    var address: Address,
    var features: Features,
    var lotSize: Int,
    var url: String,
    var community: List<CommunityFeature>,
    var size: String,
    var price: String,
    var floorPlans: List<FloorPlan>,
    var leaseTerm: String,
    var photoCount: Int,
    var photos: List<Photo>,
    var office: Office,
    var beds: Int,
    var baths: Int,
    var schools: List<School>
)