package org.martellina.rickandmorty.network.model

import com.google.gson.annotations.SerializedName

data class Locations (
    @field:SerializedName("results")
    val results: List<LocationInfo>,
    @field:SerializedName("info")
    val info: InfoResult
    )