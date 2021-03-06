package com.nguyen.shelter.api.mapper

import com.nguyen.shelter.api.entity.PropertyDetailNetworkEntity
import com.nguyen.shelter.api.response.*
import com.nguyen.shelter.model.PropertyDetail
import com.nguyen.shelter.util.EntityMapper
import javax.inject.Inject

class PropertyDetailNetworkMapper
@Inject
constructor() : EntityMapper<PropertyDetailNetworkEntity, PropertyDetail>{

    override fun mapFromEntity(entity: PropertyDetailNetworkEntity): PropertyDetail {
        return PropertyDetail(
            id = if(checkNullFromApi(entity.id)) entity.id!! else "",
            listingId = if(checkNullFromApi(entity.listingId)) entity.listingId!! else "",
            category = if(checkNullFromApi(entity.category)) entity.category!! else "",
            status = if(checkNullFromApi(entity.status)) entity.status!! else "",
            address = if(checkNullFromApi(entity.address)) entity.address!! else Address(),
            features = if(checkNullFromApi(entity.features)) entity.features!! else Features(),
            photoCount = if(checkNullFromApi(entity.category)) entity.photoCount!! else 0,
            photos = if(checkNullFromApi(entity.photos)) entity.photos!! else listOf(),
            beds = if(checkNullFromApi(entity.beds)) entity.beds!! else 0,
            baths = if(checkNullFromApi(entity.baths)) entity.baths!! else 0,
            yearBuilt = if(checkNullFromApi(entity.yearBuilt)) entity.yearBuilt!!.toString() else "Unknown",
            description = if(checkNullFromApi(entity.description)) entity.description!! else "No description",
            lotSize = if(checkNullFromApi(entity.lotSize?.size)) entity.lotSize!!.size!! else 0,
            url = if(checkNullFromApi(entity.url)) entity.url!! else "url is not available",
            community = if(checkNullFromApi(entity.community)) entity.community!! else listOf(),
            floorPlans = if(checkNullFromApi(entity.floorPlans)) entity.floorPlans!! else listOf(),
            leaseTerm = if(checkNullFromApi(entity.leaseTerm)) entity.leaseTerm!! else "Lease term not available",
            office = if(checkNullFromApi(entity.office)) entity.office!! else Office(),
            schools = if(checkNullFromApi(entity.schools)) entity.schools!! else listOf(),
            size = if(checkNullFromApi(entity.buildingSize?.size)) entity.buildingSize?.size!! else "N/A",
            price = entity.price ?: "N/A"
        )
    }

    override fun mapToEntity(domainModel: PropertyDetail): PropertyDetailNetworkEntity {
        return PropertyDetailNetworkEntity(
            id = domainModel.id,
            listingId = domainModel.listingId,
            category = domainModel.category,
            status = domainModel.status,
            address = domainModel.address,
            features = domainModel.features,
            photoCount = domainModel.photoCount,
            photos = domainModel.photos,
            beds = domainModel.beds,
            baths = domainModel.baths,
            yearBuilt = domainModel.yearBuilt.toInt(),
            description = domainModel.description,
            lotSize = LotSize(domainModel.lotSize),
            url = domainModel.url,
            community = domainModel.community,
            floorPlans = domainModel.floorPlans,
            leaseTerm = domainModel.leaseTerm,
            office = domainModel.office,
            schools = domainModel.schools,
            buildingSize = BuildingSize(domainModel.size),
            price = domainModel.price
        )
    }

    fun mapFromEntityList(entities: List<PropertyDetailNetworkEntity>): List<PropertyDetail>{
        return entities.map { mapFromEntity(it) }
    }

    private fun checkNullFromApi(data: Any?): Boolean{
        return data != null
    }

}