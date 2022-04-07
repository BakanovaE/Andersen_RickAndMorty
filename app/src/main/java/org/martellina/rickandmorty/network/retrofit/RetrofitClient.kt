package org.martellina.rickandmorty.network.retrofit

import org.martellina.rickandmorty.network.api.CharactersApi
import org.martellina.rickandmorty.network.api.EpisodesApi
import org.martellina.rickandmorty.network.api.LocationsApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private var retrofit: Retrofit? = null

    fun getClient(baseUrl: String) : Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }

    private const val BASE_URL = "https://rickandmortyapi.com/api/"
    val locationsApi: LocationsApi
        get() = RetrofitClient.getClient(BASE_URL).create(LocationsApi::class.java)

    val episodesApi: EpisodesApi
        get() = RetrofitClient.getClient(BASE_URL).create(EpisodesApi::class.java)

    val charactersApi: CharactersApi
        get() = RetrofitClient.getClient(BASE_URL).create(CharactersApi::class.java)
}