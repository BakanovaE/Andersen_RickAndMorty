package org.martellina.rickandmorty

import android.accounts.NetworkErrorException
import org.martellina.rickandmorty.data.dao.CharacterDao
import org.martellina.rickandmorty.data.dao.EpisodeDao
import org.martellina.rickandmorty.data.dao.LocationDao
import org.martellina.rickandmorty.data.entity.EpisodeDB
import org.martellina.rickandmorty.data.mappers.CharacterMapper
import org.martellina.rickandmorty.data.mappers.EpisodeMapper
import org.martellina.rickandmorty.network.api.CharactersApi
import org.martellina.rickandmorty.network.api.EpisodesApi
import org.martellina.rickandmorty.network.model.EpisodeInfo
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.martellina.rickandmorty.data.RepositoryImpl
import org.martellina.rickandmorty.data.mappers.LocationMapper
import org.martellina.rickandmorty.network.api.LocationsApi
import retrofit2.await

class RepositoryTestEpisode {

    private val testId = 1
    private val episodeNetwork = EpisodeInfo(testId, "Pilot", "", "", listOf())
    private val episodeDB = EpisodeDB(testId, "Pilot", "", "", listOf())

    @Test
    fun getEpisodeWithNetworkError() {
        runBlocking {


            val episodesApi = mockk<EpisodesApi> {
                coEvery { getEpisodeById(testId)
                } throws NetworkErrorException()
            }

            val locationDao = mockk<LocationDao>()
            val characterDao = mockk<CharacterDao>()
            val charactersMapper = mockk<CharacterMapper>()
            val locationMapper = mockk<LocationMapper>()
            val charactersApi = mockk<CharactersApi>()
            val locationsApi = mockk<LocationsApi>()

            val episodeMapper = mockk<EpisodeMapper>{
                every { mapFromDBToNetwork(episodeDB)
                } returns episodeNetwork
            }

            val episodeDao = mockk<EpisodeDao> {
                coEvery {getEpisodeById(testId)
                } returns episodeDB
            }

            val repository = RepositoryImpl(episodesApi, charactersApi, locationsApi, characterDao, episodeDao, locationDao, charactersMapper, episodeMapper, locationMapper)
            repository.getEpisodeById(testId)

            coVerify {
                episodesApi.getEpisodeById(testId).await().name
                episodeDao.getEpisodeById(testId).name
            }
        }
    }
}
