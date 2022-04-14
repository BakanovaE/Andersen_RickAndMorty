package org.martellina.rickandmorty.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import org.martellina.rickandmorty.data.converters.EpisodesConverter

@Entity (tableName = "characters")
data class CharacterDB (
        @PrimaryKey
        val id: Int,
        val name: String,
        val status: String,
        val species: String,
        val type: String,
        val gender: String,
        val originName: String,
        val originUrl: String,
        val locationName: String,
        val locationUrl: String,
        val image: String,
        @field:TypeConverters(EpisodesConverter::class)
        val episode: List<String>
        ) {

}