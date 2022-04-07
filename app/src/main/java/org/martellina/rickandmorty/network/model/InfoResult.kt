package org.martellina.rickandmorty.network.model

import com.google.gson.annotations.SerializedName

data class InfoResult(
    @field:SerializedName("pages")
    val pages: Int
)