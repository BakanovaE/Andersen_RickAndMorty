package org.martellina.rickandmorty.data.mappers

import org.martellina.rickandmorty.data.entity.LocationDB
import org.martellina.rickandmorty.network.model.LocationInfo
import javax.inject.Inject

class LocationMapper @Inject constructor() {

    fun mapFromNetworkToDB(location: LocationInfo) : LocationDB {
        return LocationDB(
            id = location.id,
            name = location.name,
            type = location.type,
            dimension = location.dimension,
            residents = location.characters
        )
    }

    fun mupFromDBToNetwork(location: LocationDB) : LocationInfo {
        return LocationInfo(
            id = location.id,
            name = location.name,
            type = location.type,
            dimension = location.dimension,
            characters = location.residents
        )
    }

    fun mupLocationsFromDBToNetwork(locations: List<LocationDB>) : List<LocationInfo> {
        return locations.map {
            LocationInfo(
                id = it.id,
                name = it.name,
                type = it.type,
                dimension = it.dimension,
                characters = it.residents
            )
        }
    }

}