package com.nguyen.shelter.model

import retrofit2.http.Query

data class PropertyFilter(
    var city: String = "New York City",
    var stateCode: String = "NY",
    var postalCode: String? = null,
    var ageMin: Int? = 0,
    var ageMax: Int? = 30,
    var bedsMin: Int? = 0,
    var bedsMax: Int? = 5,
    var bathsMin: Int? = 0,
    var bathsMax: Int? = 5,
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
    var type: String? = "any",
    var sort: String? = null,
    var allow_cats: Boolean? = false,
    var allow_dogs: Boolean? = true,
    var features: String? = null
)
