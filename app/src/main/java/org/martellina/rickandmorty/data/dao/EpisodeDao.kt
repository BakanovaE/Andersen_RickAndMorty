package org.martellina.rickandmorty.data.dao

import android.view.inspector.PropertyMapper
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import org.martellina.rickandmorty.data.entity.EpisodeDB

@Dao
interface EpisodeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveEpisode(episode: EpisodeDB)

    @Query("SELECT * FROM episodes WHERE (:name IS NULL OR name LIKE '%' || :name || '%') " +
            "AND (:episode IS NULL OR episode LIKE '%' || :episode || '%')")
    fun getFilteredEpisodes(name: String?, episode: String?): List<EpisodeDB>

    @Query("SELECT * FROM episodes WHERE id = :id")
        fun getEpisodeById(id: Int): EpisodeDB

}