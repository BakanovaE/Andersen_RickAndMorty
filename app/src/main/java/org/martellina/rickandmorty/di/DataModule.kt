package org.martellina.rickandmorty.di

import android.content.Context
import dagger.Module
import dagger.Provides
import org.martellina.rickandmorty.data.Database
import org.martellina.rickandmorty.network.Repository
import org.martellina.rickandmorty.network.api.CharactersApi
import org.martellina.rickandmorty.network.api.EpisodesApi
import org.martellina.rickandmorty.network.api.LocationsApi
import org.martellina.rickandmorty.network.model.Episodes
import org.martellina.rickandmorty.network.retrofit.RetrofitClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class DataModule {
//
//    @Provides
//    @Singleton
//    fun provideDB(context: Context): Database {
//        return Database.getDatabase(context)
//    }
//
//    @Provides
//    @Singleton
//    fun provideRetrofit() : Retrofit {
//        return Retrofit.Builder()
//            .baseUrl("https://rickandmortyapi.com/api/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }
//
//    @Provides
//    @Singleton
//    fun provideCharactersApi(retrofit: Retrofit): CharactersApi {
//        return retrofit.create(CharactersApi::class.java)
//    }
//
//    @Provides
//    @Singleton
//    fun provideEpisodesApi(retrofit: Retrofit): EpisodesApi {
//        return retrofit.create(EpisodesApi::class.java)
//    }
//
//    @Provides
//    @Singleton
//    fun provide(retrofit: Retrofit): LocationsApi {
//        return retrofit.create(LocationsApi::class.java)
//    }
//
//    @Provides
//    @Singleton
//    fun provideRepository(): Repository {
//        return Repository.get()
//    }

}