package org.martellina.rickandmorty.network.model

import com.google.gson.annotations.SerializedName

data class Episodes(
    @field:SerializedName("results")
    val results: List<EpisodeInfo>,
    @field:SerializedName("info")
    val info: InfoResult
)