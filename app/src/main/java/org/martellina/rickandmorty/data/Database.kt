package org.martellina.rickandmorty.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.martellina.rickandmorty.data.dao.CharacterDao
import org.martellina.rickandmorty.data.dao.EpisodeDao
import org.martellina.rickandmorty.data.dao.LocationDao
import org.martellina.rickandmorty.data.entity.CharacterDB
import org.martellina.rickandmorty.data.entity.EpisodeDB
import org.martellina.rickandmorty.data.entity.LocationDB

@Database(entities = [CharacterDB::class, LocationDB::class, EpisodeDB::class], version = 1)
abstract class Database : RoomDatabase() {

    abstract fun getCharacterDao() : CharacterDao
    abstract fun getLocationDao() : LocationDao
    abstract fun getEpisodeDao(): EpisodeDao

}