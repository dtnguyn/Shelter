package com.nguyen.shelter.api.mapper

import com.nguyen.shelter.api.entity.PropertyNetworkEntity
import com.nguyen.shelter.api.response.Address
import com.nguyen.shelter.api.response.BuildingSize
import com.nguyen.shelter.api.response.Features
import com.nguyen.shelter.model.Property
import com.nguyen.shelter.util.EntityMapper
import javax.inject.Inject

class PropertyNetworkMapper
@Inject
constructor() : EntityMapper<PropertyNetworkEntity, Property>{

    override fun mapFromEntity(entity: PropertyNetworkEntity): Property {
        return Property(
            id = if(checkNullFromApi(entity.id)) entity.id!! else "N/A",
            listingId = if(checkNullFromApi(entity.listingId)) entity.listingId!! else "N/A",
            category = if(checkNullFromApi(entity.category)) entity.category!! else "N/A",
            status = if(checkNullFromApi(entity.status)) entity.status!! else "N/A",
            address = if(checkNullFromApi(entity.address)) entity.address!! else Address(),
            features = if(checkNullFromApi(entity.features)) entity.features!! else Features(),
            photoCount = if(checkNullFromApi(entity.category)) entity.photoCount!! else 0,
            photos = if(checkNullFromApi(entity.photos)) entity.photos!! else listOf(),
            beds = if(checkNullFromApi(entity.beds)) entity.beds!! else 0,
            baths = if(checkNullFromApi(entity.baths)) entity.baths!! else 0,
            price = if(checkNullFromApi(entity.price)) entity.price!! else 0,
            thumbnail = if(checkNullFromApi(entity.thumbnail)) entity.thumbnail!! else "N/A",
            buildingSize = if(checkNullFromApi(entity.buildingSize?.size)) entity.buildingSize?.size!! else "N/A"
        )
    }

    override fun mapToEntity(domainModel: Property): PropertyNetworkEntity {
        return PropertyNetworkEntity(
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
            price = domainModel.price,
            thumbnail = domainModel.thumbnail,
            buildingSize = BuildingSize(domainModel.buildingSize)
        )
    }


    fun mapFromEntityList(entities: List<PropertyNetworkEntity>): List<Property>{
        return entities.map { mapFromEntity(it) }
    }

    private fun checkNullFromApi(data: Any?): Boolean{
        return data != null
    }

}