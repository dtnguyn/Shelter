package com.nguyen.shelter.db.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nguyen.shelter.api.response.*
import com.nguyen.shelter.db.entity.PropertyCacheEntity
import com.nguyen.shelter.db.entity.PropertyDetailCacheEntity
import com.nguyen.shelter.model.Property
import com.nguyen.shelter.model.PropertyDetail
import com.nguyen.shelter.util.EntityMapper
import javax.inject.Inject

class PropertyDetailCacheMapper
@Inject
constructor(
    private val gson: Gson
) : EntityMapper<PropertyDetailCacheEntity, PropertyDetail>{
    override fun mapFromEntity(entity: PropertyDetailCacheEntity): PropertyDetail {
        return PropertyDetail(
            id = entity.id,
            listingId = if(checkNull(entity.listingId)) entity.listingId!! else "N/A",
            category = if(checkNull(entity.category)) entity.category!! else "N/A",
            yearBuilt = if(checkNull(entity.yearBuilt)) entity.yearBuilt!! else "N/A",
            description = if(checkNull(entity.description)) entity.description!! else "N/A",
            status = if(checkNull(entity.status)) entity.status!! else "N/A",
            address = gson.fromJson(entity.address, Address::class.java),
            features = gson.fromJson(entity.features, Features::class.java),
            lotSize = if(checkNull(entity.lotSize)) entity.lotSize!! else 0,
            url = if(checkNull(entity.url)) entity.url!! else "N/A",
            community = gson.fromJson(entity.community, object : TypeToken<List<CommunityFeature>>() {}.type),
            floorPlans = gson.fromJson(entity.floorPlans, object : TypeToken<List<FloorPlan>>() {}.type),
            leaseTerm = if(checkNull(entity.leaseTerm)) entity.leaseTerm!! else "N/A",
            photoCount = if(checkNull(entity.photoCount)) entity.photoCount!! else 0,
            photos = gson.fromJson(entity.photos, object : TypeToken<List<Photo>>() {}.type),
            office = gson.fromJson(entity.office, Office::class.java),
            beds = if(checkNull(entity.beds)) entity.beds!! else 0,
            baths = if(checkNull(entity.baths)) entity.baths!! else 0,
            schools = gson.fromJson(entity.schools, object : TypeToken<List<School>>() {}.type),
        )
    }

    override fun mapToEntity(domainModel: PropertyDetail): PropertyDetailCacheEntity {
        return PropertyDetailCacheEntity(
            id = domainModel.id,
            listingId = domainModel.listingId,
            category = domainModel.category,
            yearBuilt = domainModel.yearBuilt,
            description = domainModel.description,
            status = domainModel.status,
            address = gson.toJson(domainModel.address),
            features = gson.toJson(domainModel.features),
            lotSize = domainModel.lotSize,
            url = domainModel.url,
            community = gson.toJson(domainModel.community),
            floorPlans = gson.toJson(domainModel.floorPlans),
            leaseTerm = domainModel.leaseTerm,
            photoCount = domainModel.photoCount,
            photos = gson.toJson(domainModel.photos),
            office = gson.toJson(domainModel.office),
            beds = domainModel.beds,
            baths = domainModel.baths,
            schools = gson.toJson(domainModel.schools)
        )
    }

    private fun checkNull(data: Any?): Boolean{
        return data != null
    }

}