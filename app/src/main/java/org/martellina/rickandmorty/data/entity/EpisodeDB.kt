package org.martellina.rickandmorty.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import org.martellina.rickandmorty.data.converters.CharactersConverter

@Entity(tableName = "episodes")
data class EpisodeDB (
        @PrimaryKey
        val id: Int,
        val name: String,
        val air_date: String,
        val episode: String,
        @field:TypeConverters(CharactersConverter::class)
        val characters: List<String>?
) {
}