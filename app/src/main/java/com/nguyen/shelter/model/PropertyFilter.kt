package com.nguyen.shelter.model

import retrofit2.http.Query

data class PropertyFilter(
    var city: String = "New York City",
    var stateCode: String = "NY",
    var postalCode: String? = null,
    var ageMin: Int? = null,
    var ageMax: Int? = null,
    var bedsMin: Int? = 2,
    var bedsMax: Int? = 7,
    var bathsMin: Int? = 2,
    var bathsMax: Int? = 7,
    var priceMin: Int? = 1000,
    var priceMax: Int? = 7000,
    var lotMin: Int? = 1000,
    var lotMax: Int? = 7000,
    var areaMax: Int? = 1000,
    var areaMin: Int? = 7000,
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
