package org.martellina.rickandmorty.network.api

import org.martellina.rickandmorty.network.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharactersApi {
    @GET("character")
    fun getAllCharacters(@Query("page") page: Int,
                         @Query("name") name: String?,
                         @Query("status") status: String?,
                         @Query("species") species: String?,
                         @Query("type") type: String?,
                         @Query("gender") gender: String?) : Call<Characters>

    @GET("character/{id}")
    fun getCharacterById(@Path("id") id: Int) : Call<CharacterInfo>

    @GET("character/{id}")
    suspend fun getCharactersById(@Path("id") id: String): List<CharacterInfo>

    @GET("character")
    fun getFilteredCharacters(@Query("name") name: String?,
                              @Query("status") status: String?,
                              @Query("species") species: String?,
                             @Query("type") type: String?,
                             @Query("gender") gender: String?) : Call<Characters>
}