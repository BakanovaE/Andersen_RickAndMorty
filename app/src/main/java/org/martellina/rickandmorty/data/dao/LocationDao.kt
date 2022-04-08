package org.martellina.rickandmorty.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.martellina.rickandmorty.data.entity.LocationDB

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveLocations(location: LocationDB)

    @Query("SELECT * FROM locations WHERE (:name IS NULL OR name LIKE '%' || :name || '%') " +
            "AND (:type IS NULL OR type LIKE '%' || :type || '%')" +
            "And (:dimension IS NULL OR dimension LIKE '%' || :dimension || '%')")
    fun getFilteredLocations (name: String?, type: String?, dimension: String?): List<LocationDB>

    @Query("SELECT * FROM locations")
    fun getAllLocations(): List<LocationDB>

    @Query("SELECT * FROM locations WHERE id = :id")
    fun getLocationById(id: Int): LocationDB
}