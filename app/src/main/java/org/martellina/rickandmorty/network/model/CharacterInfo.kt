package org.martellina.rickandmorty.network.model

import com.google.gson.annotations.SerializedName

data class CharacterInfo (
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("species")
    val species: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("origin")
    val origin: CharacterLocation,
    @SerializedName("location")
    val location: CharacterLocation,
    @SerializedName("episode")
    val episode: List<String>
)