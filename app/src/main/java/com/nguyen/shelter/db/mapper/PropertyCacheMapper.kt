package com.nguyen.shelter.db.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nguyen.shelter.api.response.Address
import com.nguyen.shelter.api.response.Features
import com.nguyen.shelter.api.response.Photo
import com.nguyen.shelter.db.entity.PropertyCacheEntity
import com.nguyen.shelter.model.Property
import com.nguyen.shelter.util.EntityMapper
import javax.inject.Inject

class PropertyCacheMapper
@Inject
constructor(
    private val gson: Gson
) : EntityMapper<PropertyCacheEntity, Property>{

    override fun mapFromEntity(entity: PropertyCacheEntity): Property {
        return Property(
            id = if(checkNull(entity.id)) entity.id else "",
            listingId = if(checkNull(entity.listingId)) entity.listingId!! else "",
            category = if(checkNull(entity.category)) entity.category!! else "",
            status = if(checkNull(entity.status)) entity.status!! else "",
            address = gson.fromJson(entity.address, Address::class.java),
            features = gson.fromJson(entity.features, Features::class.java),
            photoCount = if(checkNull(entity.category)) entity.photoCount!! else 0,
            photos = gson.fromJson(entity.photos, object : TypeToken<List<Photo>>() {}.type),
            beds = if(checkNull(entity.beds)) entity.beds!! else 0,
            baths = if(checkNull(entity.baths)) entity.baths!! else 0,
            price = if(checkNull(entity.price)) entity.price!! else 0,
            thumbnail = if(checkNull(entity.thumbnail)) entity.thumbnail!! else "N/A",
            buildingSize = if(checkNull(entity.buildingSize)) entity.buildingSize!! else "N/A"
        )
    }

    override fun mapToEntity(domainModel: Property): PropertyCacheEntity {
        return PropertyCacheEntity(
            id = domainModel.id,
            listingId = domainModel.listingId,
            category = domainModel.category,
            status = domainModel.status,
            address = gson.toJson(domainModel.address),
            features = gson.toJson(domainModel.features),
            photoCount = domainModel.photoCount,
            photos = gson.toJson(domainModel.photos),
            beds = domainModel.beds,
            baths = domainModel.baths,
            price = domainModel.price,
            thumbnail = domainModel.thumbnail,
            buildingSize = domainModel.buildingSize
        )
    }


    fun mapFromEntityList(entities: List<PropertyCacheEntity>): List<Property>{
        return entities.map { mapFromEntity(it) }
    }

    fun mapToEntityList(entities: List<Property>): List<PropertyCacheEntity>{
        return entities.map { mapToEntity(it) }
    }

    private fun checkNull(data: Any?): Boolean{
        return data != null
    }


}