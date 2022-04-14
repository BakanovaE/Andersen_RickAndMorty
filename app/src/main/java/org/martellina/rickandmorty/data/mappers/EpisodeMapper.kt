package org.martellina.rickandmorty.data.mappers

import org.martellina.rickandmorty.data.entity.EpisodeDB
import org.martellina.rickandmorty.network.model.EpisodeInfo
import javax.inject.Inject

class EpisodeMapper @Inject constructor() {

    fun mapFromNetworkToDB(episode: EpisodeInfo) : EpisodeDB {

        return EpisodeDB(
            id = episode.id,
            name = episode.name,
            air_date = episode.air_date,
            episode = episode.episode,
            characters = episode.characters,
            )
    }

    fun mapFromDBToNetwork(episode: EpisodeDB) : EpisodeInfo {

        return EpisodeInfo(
            id = episode.id,
            name = episode.name,
            air_date = episode.air_date,
            episode = episode.episode,
            characters = episode.characters,
        )
    }


    fun mapEpisodesFromDBToNetwork(episodes: List<EpisodeDB>) : List<EpisodeInfo> {
        return episodes.map {
            EpisodeInfo(
                id = it.id,
                name = it.name,
                air_date = it.air_date,
                episode = it.episode,
                characters = it.characters,
            )
        }
    }
}