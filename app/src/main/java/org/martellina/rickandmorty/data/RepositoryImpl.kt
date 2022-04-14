package org.martellina.rickandmorty.data

import org.martellina.rickandmorty.data.dao.CharacterDao
import org.martellina.rickandmorty.data.dao.EpisodeDao
import org.martellina.rickandmorty.data.dao.LocationDao
import org.martellina.rickandmorty.data.mappers.CharacterMapper
import org.martellina.rickandmorty.data.mappers.EpisodeMapper
import org.martellina.rickandmorty.data.mappers.LocationMapper
import org.martellina.rickandmorty.network.api.CharactersApi
import org.martellina.rickandmorty.network.api.EpisodesApi
import org.martellina.rickandmorty.network.api.LocationsApi
import org.martellina.rickandmorty.network.model.*
import retrofit2.await
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryImpl @Inject constructor(private val episodesApi: EpisodesApi,
                                         private val charactersApi: CharactersApi,
                                         private val locationsApi: LocationsApi,
                                         private val characterDao: CharacterDao,
                                         private val episodeDao: EpisodeDao,
                                         private val locationDao: LocationDao,
                                         private val characterMapper: CharacterMapper,
                                         private val episodeMapper: EpisodeMapper,
                                         private val locationMapper: LocationMapper
                                         ): Repository{

    override suspend fun getAllEpisodes(page: Int, filter: EpisodesFilter): Episodes? {
        var result: Episodes?
        try {
            result = episodesApi.getAllEpisodes(page, filter.name, filter.code).await()
            for (episode in result.results) {
                episodeDao.saveEpisode(episodeMapper.mapFromNetworkToDB(episode))
            }
        } catch (e: Exception) {
            val episodes = episodeMapper.mapEpisodesFromDBToNetwork(episodeDao.getFilteredEpisodes(filter.name, filter.code))
            result = Episodes(episodes, InfoResult(1))
        }
        return result
    }

    override suspend fun getFilteredEpisodes(filter: EpisodesFilter): Episodes? {
        val result: Episodes? = try {
            episodesApi.getFilteredEpisodes(filter.name, filter.code).await()
        } catch (e: Exception) {
            val episodes = episodeMapper.mapEpisodesFromDBToNetwork(episodeDao.getFilteredEpisodes(filter.name, filter.code))
            Episodes(episodes, InfoResult(1))
        }
        return result
    }

    override suspend fun getCharacterById(id: Int): CharacterInfo? {
        var result: CharacterInfo? = null
        try {
            result = charactersApi.getCharacterById(id).await()
        } catch (e :Exception) {
            val character = characterDao.getCharacterById(id)
            if (character != null) {
                result = characterMapper.mapFromDBToNetwork(characterDao.getCharacterById(id))
            }
        }
        return result
    }

    override suspend fun getAllCharacters(page: Int, filter: CharactersFilter) : Characters? {
        var result: Characters?
        try {
            result = charactersApi.getAllCharacters(page, filter.name, filter.status, filter.species, filter.type, filter.gender).await()
            result.results.forEach { character ->
                characterDao.saveCharacters(characterMapper.mapFromNetworkToDB(character))
            }
        } catch (e: Exception) {
            val characters = characterMapper.mapCharactersFromDBToNetwork(characterDao.getFilteredCharacters1(filter.name, filter.status, filter.species, filter.type, filter.gender))
            result = Characters(characters, InfoResult(1))
        }
        return result
    }

    override suspend fun getEpisodeById(id: Int): EpisodeInfo? {
        var result: EpisodeInfo? = null
        try {
            result = episodesApi.getEpisodeById(id).await()
        } catch (e: Exception) {
            val episode = episodeDao.getEpisodeById(id)
            if (episode != null) {
            result = episodeMapper.mapFromDBToNetwork(episode)
            }
        }
        return result
    }

    override suspend fun getLocationById(id: Int): LocationInfo? {
        var result: LocationInfo? = null
        try {
            result = locationsApi.getLocationById(id).await()
        } catch (e: Exception) {
            val location = locationDao.getLocationById(id)
            if (location != null) {
                result = locationMapper.mupFromDBToNetwork(location)
            }
        }
        return result
    }

    override suspend fun getAllLocations(page: Int, filter: LocationsFilter) : Locations? {
        var result: Locations?
        try {
            result = locationsApi.getAllLocations(page, filter.name, filter.type, filter.dimension).await()
            result.results.forEach { location ->
                locationDao.saveLocations(locationMapper.mapFromNetworkToDB(location))
            }
        } catch (e: Exception) {
            val locations = locationMapper.mupLocationsFromDBToNetwork(locationDao.getFilteredLocations(filter.name, filter.type, filter.dimension))
            result = Locations(locations, InfoResult(1))
        }
        return result
    }

    override suspend fun getFilteredLocations(filter: LocationsFilter): Locations? {
        val result: Locations? = try {
            locationsApi.getFilteredLocations(filter.name, filter.type, filter.dimension).await()
        } catch (e: Exception) {
            val locations = locationMapper.mupLocationsFromDBToNetwork(locationDao.getFilteredLocations(filter.name, filter.type, filter.dimension))
            Locations(locations, InfoResult(1))
        }
        return result
    }

    override suspend fun getFilteredCharacters(filter: CharactersFilter): Characters? {
        val result: Characters? = try {
            charactersApi.getFilteredCharacters(filter.name, filter.status, filter.species, filter.type, filter.gender).await()
        } catch (e: Exception) {
            val ch = characterDao.getFilteredCharacters1(filter.name, filter.status, filter.species, filter.type, filter.gender)
            val characters = characterMapper.mapCharactersFromDBToNetwork(ch)
            Characters(characters, InfoResult(1))
        }
        return result
    }
}
