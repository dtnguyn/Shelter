package com.nguyen.shelter.repo

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.nguyen.shelter.api.entity.PropertyNetworkEntity
import com.nguyen.shelter.api.mapper.PropertyNetworkMapper
import com.nguyen.shelter.api.service.RealtorApiService
import com.nguyen.shelter.db.ShelterDatabase
import com.nguyen.shelter.db.entity.PropertyCacheEntity
import com.nguyen.shelter.db.entity.RemoteKeysEntity
import com.nguyen.shelter.db.mapper.PropertyCacheMapper
import com.nguyen.shelter.model.PropertyFilter
import retrofit2.HttpException
import java.io.IOException
import java.io.InvalidObjectException


private const val STARTING_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
class SaleRemoteMediator
constructor
    (
    private val networkMapper: PropertyNetworkMapper,
    private val cacheMapper: PropertyCacheMapper,
    private val filter: PropertyFilter,
    private val service: RealtorApiService,
    private val database: ShelterDatabase
): RemoteMediator<Int, PropertyCacheEntity>() {

    companion object{
        private var isLoaded: Boolean = true
    }


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PropertyCacheEntity>
    ): MediatorResult {
        println("debug: Sale Loading...")
        val page = when(loadType){
            LoadType.REFRESH -> {
                if(isLoaded) return MediatorResult.Success(endOfPaginationReached = true)
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
//            {
//
//                state
//                val remoteKeys = getRemoteKeyForFirstItem(state)
//                if (remoteKeys == null) {
//                    throw InvalidObjectException("Remote key and the prevKey should not be null")
//                }
//                // If the previous key is null, then we can't request more data
//                val prevKey = remoteKeys.prevKey
//                if (prevKey == null) {
//                    return MediatorResult.Success(endOfPaginationReached = true)
//                }
//                remoteKeys.prevKey
//            }
            LoadType.APPEND -> return MediatorResult.Success(endOfPaginationReached = true)
//            {
//                val remoteKeys = getRemoteKeyForLastItem(state)
//                if (remoteKeys?.nextKey == null) {
//                    throw InvalidObjectException("Remote key should not be null for $this")
//                }
//                remoteKeys.nextKey
//            }
        }

        try {
            val apiResponse = service.getPropertiesForSale(
                city = filter.city,
                stateCode = filter.stateCode,
                limit = state.config.pageSize,
                offset = page,
                postalCode = filter.postalCode,
                ageMin = filter.ageMin,
                ageMax = filter.ageMax,
                bedsMin = filter.bedsMin,
                bedsMax = filter.bedsMax,
                bathsMin = filter.bathsMin,
                bathsMax = filter.bathsMax,
                priceMin = filter.priceMin,
                priceMax = filter.priceMax,
                lotMin = filter.lotMin,
                lotMax = filter.lotMax,
                areaMin = filter.areaMin,
                areaMax = filter.areaMax,
                isForeclosure = filter.isForeclosure,
                hasOpenHouse = filter.hasOpenHouse,
                isPending = filter.isPending,
                isNewPlan = filter.isNewPlan,
                isContingent = filter.isContingent,
                isNewConstruction = filter.isNewConstruction,
                type = filter.type,
                sort = filter.sort,
                allowCats = filter.allow_cats,
                allowDogs =  filter.allow_dogs,
                features = filter.features
            )
            isLoaded = true
            var properties = apiResponse.data
            if(properties == null) properties = listOf()
            val endOfPaginationReached = properties.isEmpty()
            database.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeysDao().clearRemoteKeys()
                    database.propertyDao().clearProperties()
                }
                val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = properties.map {
                    RemoteKeysEntity(
                        id = it.id ?: throw Exception("Remote key and the prevKey should not be null"),
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }
                println("debug: keys: $keys")
                database.remoteKeysDao().insertAll(keys)
                val test = cacheMapper.mapToEntityList(networkMapper.mapFromEntityList(properties))

                database.propertyDao().insertAll(test)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }


    }

    private suspend fun LoadType.getPage(state: PagingState<Int, PropertyCacheEntity>) : Int{
        return when(this){
            LoadType.REFRESH -> {
                val remoteKey = getRemoteKeyClosestToCurrentPosition(state)
                    ?: // The LoadType is PREPEND so some data was loaded before,
                    // so we should have been able to get remote keys
                    // If the remoteKeys are null, then we're an invalid state and we have a bug
                    throw InvalidObjectException("Remote key and the prevKey should not be null")
                remoteKey.nextKey?.minus(1) ?: 1
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                if (remoteKeys?.prevKey == null) {
                    throw InvalidObjectException("Remote key should not be null for $this")
                }
                return remoteKeys.nextKey!!
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                if (remoteKeys?.nextKey == null) {
                    throw InvalidObjectException("Remote key should not be null for $this")
                }
                remoteKeys.nextKey
            }
        }
    }




    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, PropertyCacheEntity>): RemoteKeysEntity? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let {
                // Get the remote keys of the last item retrieved
                database.remoteKeysDao().getRemoteKeysId(it.id )
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, PropertyCacheEntity>): RemoteKeysEntity? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let {
                // Get the remote keys of the first items retrieved
                database.remoteKeysDao().getRemoteKeysId(it.id)
            }
    }


    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, PropertyCacheEntity>
    ): RemoteKeysEntity? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.remoteKeysDao().getRemoteKeysId(id)
            }
        }
    }

}