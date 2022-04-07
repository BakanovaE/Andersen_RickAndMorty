package org.martellina.rickandmorty.network.model

import com.google.gson.annotations.SerializedName

data class Characters (
    @field:SerializedName("results")
    val results: List<CharacterInfo>,
    @field:SerializedName("info")
    val info: InfoResult
)