package org.martellina.rickandmorty.data.mappers

import org.martellina.rickandmorty.data.entity.CharacterDB
import org.martellina.rickandmorty.network.model.CharacterInfo
import org.martellina.rickandmorty.network.model.CharacterLocation
import javax.inject.Inject

class CharacterMapper @Inject constructor() {

    fun mapFromNetworkToDB(character: CharacterInfo): CharacterDB {
        return CharacterDB(
            id = character.id,
            name = character.name,
            species = character.species,
            type = character.type,
            status = character.status,
            gender = character.gender,
            image = character.image,
            originName = character.origin.name,
            originUrl = character.origin.url,
            locationName = character.location.name,
            locationUrl = character.location.url,
            episode = character.episode
        )
    }

    fun mapFromDBToNetwork(character: CharacterDB?): CharacterInfo? {
        return character?.let {
            CharacterInfo(
                id = it.id,
                name = character.name,
                species = character.species,
                type = character.type,
                status = character.status,
                gender = character.gender,
                image = character.image,
                origin = CharacterLocation(character.originName, character.originUrl),
                location = CharacterLocation(character.locationName, character.locationUrl),
                episode = character.episode
            )
        }
    }

    fun mapCharactersFromDBToNetwork(characters: List<CharacterDB>): List<CharacterInfo> {
        return characters.map {
            CharacterInfo(
                id = it.id,
                name = it.name,
                species = it.species,
                type = it.type,
                status = it.status,
                gender = it.gender,
                image = it.image,
                origin = CharacterLocation(it.originName, it.originUrl),
                location = CharacterLocation(it.locationName, it.locationUrl),
                episode = it.episode
            )
        }
    }

}