package com.nguyen.shelter.model

import retrofit2.http.Query

data class PropertyFilter(
    var city: String = "New York City",
    var stateCode: String = "NY",
    var postalCode: String? = null,
    var ageMin: Int? = null,
    var ageMax: Int? = null,
    var bedsMin: Int? = null,
    var bedsMax: Int? = null,
    var bathsMin: Int? = null,
    var bathsMax: Int? = null,
    var priceMin: Int? = null,
    var priceMax: Int? = null,
    var lotMin: Int? = null,
    var lotMax: Int? = null,
    var areaMax: Int? = null,
    var areaMin: Int? = null,
    var hasOpenHouse: Boolean? = null,
    var isPending: Boolean? = null,
    var isNewPlan: Boolean? = null,
    var isContingent: Boolean? = null,
    var isNewConstruction: Boolean? = null,
    var isForeclosure: Boolean? = null,
    var type: String? = null,
    var sort: String? = null,
    var allow_cats: Boolean? = null,
    var allow_dogs: Boolean? = null,
    var features: String? = null
)
