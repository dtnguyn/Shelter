package com.nguyen.shelter.model

import com.nguyen.shelter.api.response.Address
import com.nguyen.shelter.api.response.Features
import com.nguyen.shelter.api.response.Photo

data class Property (
    var id: String,
    var listingId: String,
    var category: String,
    var status: String,
    var address: Address,
    var features: Features,
    var photoCount: Int,
    var beds: Int,
    var baths: Int,
    var price: Int,
    var photos: List<Photo>,
    var thumbnail: String,
    var buildingSize: String
)