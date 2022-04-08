package org.martellina.rickandmorty.data

import android.content.Context
import org.martellina.rickandmorty.data.mappers.CharacterMapper
import org.martellina.rickandmorty.data.mappers.EpisodeMapper
import org.martellina.rickandmorty.data.mappers.LocationMapper
import org.martellina.rickandmorty.network.retrofit.Common
import org.martellina.rickandmorty.network.model.*
import retrofit2.await
import java.lang.Exception

class RepositoryImpl private constructor(context: Context): Repository{

    companion object {
        private var INSTANCE: RepositoryImpl? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = RepositoryImpl(context)
            }
        }

        fun get(): RepositoryImpl {
            return INSTANCE ?: throw IllegalStateException("Repository should be initialized")
        }
    }

    private val database = Database.getDatabase(context.applicationContext)

    private val episodeDao = database.getEpisodeDao()
    private val locationDao = database.getLocationDao()
    private val characterDao = database.getCharacterDao()

    private val episodeMapper = EpisodeMapper()
    private val locationMapper = LocationMapper()
    private val characterMapper = CharacterMapper()

    private val episodesApi = Common.episodesApi
    private val charactersApi = Common.charactersApi
    private val locationsApi = Common.locationsApi

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
        val result: CharacterInfo? = try {
            charactersApi.getCharacterById(id).await()
        } catch (e :Exception) {
            characterMapper.mapFromDBToNetwork(characterDao.getCharacterById(id))
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
            val characters = characterMapper.mapCharactersFromDBToNetwork(characterDao.getFilteredCharacters(filter.name, filter.status, filter.species, filter.type, filter.gender))
            result = Characters(characters, InfoResult(1))
        }
        return result
    }

    override suspend fun getEpisodeById(id: Int): EpisodeInfo? {
        val result: EpisodeInfo? = try {
            episodesApi.getEpisodeById(id).await()
        } catch (e: Exception) {
            episodeMapper.mapFromDBToNetwork(episodeDao.getEpisodeById(id))
        }
        return result
    }

    override suspend fun getLocationById(id: Int): LocationInfo? {
        val result: LocationInfo? = try {
            locationsApi.getLocationById(id).await()
        } catch (e: Exception) {
            locationMapper.mupFromDBToNetwork(locationDao.getLocationById(id))
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
            val characters = characterMapper.mapCharactersFromDBToNetwork(characterDao.getFilteredCharacters(filter.name, filter.status, filter.species, filter.type, filter.gender))
            Characters(characters, InfoResult(1))
        }
        return result
    }
}