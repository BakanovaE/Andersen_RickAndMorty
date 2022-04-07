package org.martellina.rickandmorty.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import org.martellina.rickandmorty.data.converters.CharactersConverter

@Entity(tableName = "locations")
data class LocationDB (
    @PrimaryKey
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String,
    @field:TypeConverters(CharactersConverter::class)
    val residents: List<String>
        )