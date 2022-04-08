package org.martellina.rickandmorty.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import org.martellina.rickandmorty.data.Database
import org.martellina.rickandmorty.data.dao.CharacterDao
import org.martellina.rickandmorty.data.dao.EpisodeDao
import org.martellina.rickandmorty.data.dao.LocationDao
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    fun provideDB(context: Context): Database {
        return Room.databaseBuilder(context, Database::class.java, "database")
            .build()
    }

    @Provides
    @Singleton
    fun provideCharacterDao(database: Database): CharacterDao {
        return database.getCharacterDao()
    }

    @Provides
    @Singleton
    fun provideEpisodeDao(database: Database): EpisodeDao {
        return database.getEpisodeDao()
    }

    @Provides
    @Singleton
    fun provideLocationDao(database: Database): LocationDao {
        return database.getLocationDao()
    }

}