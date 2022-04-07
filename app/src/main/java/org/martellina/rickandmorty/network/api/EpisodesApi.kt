package org.martellina.rickandmorty.network.api

import org.martellina.rickandmorty.network.model.EpisodeInfo
import org.martellina.rickandmorty.network.model.Episodes
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EpisodesApi {
    @GET("episode")
    fun getAllEpisodes(@Query("page") page: Int,
                        @Query("name") name: String?,
                        @Query("episode") code: String?)
    : Call<Episodes>

    @GET("episode")
    fun getFilteredEpisodes(@Query("name") name: String?,
                            @Query("episode") code: String?)
    : Call<Episodes>

    @GET("episode/{id}")
    fun getEpisodeById(@Path("id") id: Int) : Call<EpisodeInfo>

    @GET("episode/{id}")
    suspend fun getEpisodesById(@Path("id") id: String): List<EpisodeInfo>

}