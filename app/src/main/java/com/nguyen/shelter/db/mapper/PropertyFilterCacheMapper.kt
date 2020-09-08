package com.nguyen.shelter.db.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nguyen.shelter.api.response.Address
import com.nguyen.shelter.api.response.DataList
import com.nguyen.shelter.api.response.Features
import com.nguyen.shelter.api.response.Photo
import com.nguyen.shelter.db.entity.PropertyCacheEntity
import com.nguyen.shelter.db.entity.PropertyFilterCacheEntity
import com.nguyen.shelter.model.Property
import com.nguyen.shelter.model.PropertyFilter
import com.nguyen.shelter.util.EntityMapper
import javax.inject.Inject

class PropertyFilterCacheMapper
@Inject
constructor(
    private val gson: Gson
) : EntityMapper<PropertyFilterCacheEntity, PropertyFilter>{

    override fun mapFromEntity(entity: PropertyFilterCacheEntity): PropertyFilter {
        return PropertyFilter(
            city = entity.city,
            stateCode= entity.stateCode,
            postalCode= entity.postalCode,
            ageMin= entity.ageMin,
            ageMax= entity.ageMax,
            bedsMin= entity.bedsMin,
            bedsMax= entity.bedsMax,
            bathsMin= entity.bathsMin,
            bathsMax= entity.bathsMax,
            priceMin= entity.priceMin,
            priceMax= entity.priceMax,
            lotMin= entity.lotMin,
            lotMax= entity.lotMax,
            areaMax= entity.areaMax,
            areaMin= entity.areaMin,
            hasOpenHouse= entity.hasOpenHouse,
            isPending= entity.isPending,
            isNewPlan= entity.isNewPlan,
            isContingent= entity.isContingent,
            isNewConstruction= entity.isNewConstruction,
            isForeclosure= entity.isForeclosure,
            type= entity.type,
            sort= entity.sort,
            allow_cats= entity.allow_cats,
            allow_dogs= entity.allow_dogs,
            features= entity.features
        )
    }


    override fun mapToEntity(domainModel: PropertyFilter): PropertyFilterCacheEntity {
        return PropertyFilterCacheEntity(
            filterType = "",
            city = domainModel.city,
            stateCode= domainModel.stateCode,
            postalCode= domainModel.postalCode,
            ageMin= domainModel.ageMin,
            ageMax= domainModel.ageMax,
            bedsMin= domainModel.bedsMin,
            bedsMax= domainModel.bedsMax,
            bathsMin= domainModel.bathsMin,
            bathsMax= domainModel.bathsMax,
            priceMin= domainModel.priceMin,
            priceMax= domainModel.priceMax,
            lotMin= domainModel.lotMin,
            lotMax= domainModel.lotMax,
            areaMax= domainModel.areaMax,
            areaMin= domainModel.areaMin,
            hasOpenHouse= domainModel.hasOpenHouse,
            isPending= domainModel.isPending,
            isNewPlan= domainModel.isNewPlan,
            isContingent= domainModel.isContingent,
            isNewConstruction= domainModel.isNewConstruction,
            isForeclosure= domainModel.isForeclosure,
            type= domainModel.type,
            sort= domainModel.sort,
            allow_cats= domainModel.allow_cats,
            allow_dogs= domainModel.allow_dogs,
            features= domainModel.features
        )
    }


    private fun checkNullFromApi(data: Any?): Boolean{
        return data != null
    }

}