package org.martellina.rickandmorty

import junit.framework.Assert.assertEquals
import org.junit.Test
import org.martellina.rickandmorty.data.entity.CharacterDB
import org.martellina.rickandmorty.data.mappers.CharacterMapper
import org.martellina.rickandmorty.network.model.CharacterInfo
import org.martellina.rickandmorty.network.model.CharacterLocation

class MapperCharacterTest {
    private val characterNetwork = CharacterInfo(1, "Rick Sanchez", "", "", "", "", "", CharacterLocation("", ""), CharacterLocation("", ""), emptyList())
    private val dbCharacter = CharacterDB(1, "Rick Sanchez", "", "", "", "", "", "", "", "", "", emptyList())
    private val characterMapper = CharacterMapper()

    @Test
    fun testCharacterMapperFromDBToNetwork() {
        val result = characterMapper.mapFromDBToNetwork(dbCharacter)
        val expected = characterNetwork

        assertEquals(expected, result)
    }

    @Test
    fun testEpisodesConverter() {
        val expected = characterNetwork.episode

        val result = characterMapper.mapFromDBToNetwork(dbCharacter)?.episode

        assertEquals(expected, result)
    }
}