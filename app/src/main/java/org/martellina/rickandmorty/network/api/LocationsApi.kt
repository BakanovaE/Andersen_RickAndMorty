package org.martellina.rickandmorty.network.api

import org.martellina.rickandmorty.network.model.LocationInfo
import org.martellina.rickandmorty.network.model.Locations
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LocationsApi {
    @GET("location")
    fun getAllLocations(@Query("page") page: Int?,
                        @Query("name") name: String?,
                        @Query("type") type: String?,
                        @Query("dimension") dimension: String?) : Call<Locations>

    @GET("location")
    fun getFilteredLocations(@Query("name") name: String?,
                             @Query("type") type: String?,
                            @Query("dimension") dimension: String?) : Call<Locations>

    @GET("location/{id}")
    fun getLocationById(@Path("id") id: Int) : Call<LocationInfo>
}