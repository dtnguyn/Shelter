package com.nguyen.shelter.api.service

import com.nguyen.shelter.api.entity.PropertyDetailNetworkEntity
import com.nguyen.shelter.api.entity.PropertyNetworkEntity
import com.nguyen.shelter.api.response.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface RealtorApiService {

    @Headers(
        "x-rapidapi-host: realtor.p.rapidapi.com",
        "x-rapidapi-key: 7671356523mshf5fc94df33e09eep1dfeb2jsn2065788690cf"
    )
    @GET("list-for-rent")
    suspend fun getPropertiesForRent(
        @Query("city") city: String,
        @Query("state_code") stateCode: String,
        @Query("postal_code") postalCode: String?,
        @Query("limit")limit : Int,
        @Query("offset") offset: Int,
        @Query("beds_min") bedsMin: Int?,
        @Query("beds_max") bedsMax: Int?,
        @Query("baths_min") bathsMin: Int?,
        @Query("baths_max") bathsMax: Int?,
        @Query("price_min") priceMin: Int?,
        @Query("price_max") priceMax: Int?,
        @Query("lot_sqft_min") lotMin: Int?,
        @Query("lot_sqft_max") lotMax: Int?,
        @Query("sqft_min") areaMin: Int?,
        @Query("sqft_max") areaMax: Int?,
        @Query("prop_type") type: String?,
        @Query("sort") sort: String?,
        @Query("allow_cats") allowCats: Boolean?,
        @Query("allow_dogs") allowDogs: Boolean?,
        @Query("features") features: String?

    ): ApiResponse<PropertyNetworkEntity>


    @Headers(
        "x-rapidapi-host: realtor.p.rapidapi.com",
        "x-rapidapi-key: 7671356523mshf5fc94df33e09eep1dfeb2jsn2065788690cf"
    )
    @GET("list-for-sale")
    suspend fun getPropertiesForSale(
        @Query("city") city: String,
        @Query("state_code") stateCode: String,
        @Query("postal_code") postalCode: String?,
        @Query("limit")limit : Int,
        @Query("offset") offset: Int,
        @Query("age_min") ageMin: Int?,
        @Query("age_max") ageMax: Int?,
        @Query("beds_min") bedsMin: Int?,
        @Query("beds_max") bedsMax: Int?,
        @Query("baths_min") bathsMin: Int?,
        @Query("baths_max") bathsMax: Int?,
        @Query("price_min") priceMin: Int?,
        @Query("price_max") priceMax: Int?,
        @Query("lot_sqft_min") lotMin: Int?,
        @Query("lot_sqft_max") lotMax: Int?,
        @Query("sqft_min") areaMin: Int?,
        @Query("sqft_max") areaMax: Int?,
        @Query("has_open_house") hasOpenHouse: Boolean?,
        @Query("is_pending") isPending: Boolean?,
        @Query("is_new_plan") isNewPlan: Boolean?,
        @Query("is_contingent") isContingent: Boolean?,
        @Query("is_new_construction") isNewConstruction: Boolean?,
        @Query("is_foreclosure") isForeclosure: Boolean?,
        @Query("prop_type") type: String?,
        @Query("sort") sort: String?,
        @Query("allow_cats") allowCats: Boolean?,
        @Query("allow_dogs") allowDogs: Boolean?,
        @Query("features") features: String?

    ): ApiResponse<PropertyNetworkEntity>


    @Headers(
        "x-rapidapi-host: realtor.p.rapidapi.com",
        "x-rapidapi-key: 7671356523mshf5fc94df33e09eep1dfeb2jsn2065788690cf"
    )
    @GET("detail")
    suspend fun getPropertyDetail(@Query("property_id") id: String): ApiResponse<PropertyDetailNetworkEntity>



}