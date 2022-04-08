package org.martellina.rickandmorty.data

import org.martellina.rickandmorty.network.model.*

interface Repository {

    suspend fun getAllEpisodes(page: Int, filter: EpisodesFilter): Episodes?

    suspend fun getFilteredEpisodes(filter: EpisodesFilter): Episodes?

    suspend fun getCharacterById(id: Int): CharacterInfo?

    suspend fun getAllCharacters(page: Int, filter: CharactersFilter) : Characters?

    suspend fun getEpisodeById(id: Int): EpisodeInfo?

    suspend fun getLocationById(id: Int): LocationInfo?

    suspend fun getAllLocations(page: Int, filter: LocationsFilter) : Locations?

    suspend fun getFilteredLocations(filter: LocationsFilter): Locations?

    suspend fun getFilteredCharacters(filter: CharactersFilter): Characters?
}