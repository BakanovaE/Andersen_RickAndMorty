package org.martellina.rickandmorty.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.martellina.rickandmorty.network.api.CharactersApi
import org.martellina.rickandmorty.network.api.EpisodesApi
import org.martellina.rickandmorty.network.api.LocationsApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class RetrofitModule {

    @Singleton
    @Provides
    fun provideRetrofit(
        gson: Gson,
        okHttpClient: OkHttpClient
    ) = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        .build()

    @Singleton
    @Provides
    fun provideGson() = GsonBuilder().create()

    @Singleton
    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ) = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .build()

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    companion object {
        const val BASE_URL = "https://rickandmortyapi.com/api/"
    }

    @Singleton
    @Provides
    fun provideCharactersApi(retrofit: Retrofit): CharactersApi {
        return retrofit.create(CharactersApi::class.java)
    }

    @Singleton
    @Provides
    fun provideEpisodesApi(retrofit: Retrofit): EpisodesApi {
        return retrofit.create(EpisodesApi::class.java)
    }

    @Singleton
    @Provides
    fun provideLocationsApi(retrofit: Retrofit): LocationsApi {
        return retrofit.create(LocationsApi::class.java)
    }

}