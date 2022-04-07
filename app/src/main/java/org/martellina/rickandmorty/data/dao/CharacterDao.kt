package org.martellina.rickandmorty.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.martellina.rickandmorty.data.entity.CharacterDB
import org.martellina.rickandmorty.network.model.CharacterInfo

@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveCharacters(character: CharacterDB)

    @Query("SELECT * FROM characters WHERE (:name IS NULL OR name LIKE :name) " +
            "AND (:status IS NULL OR status LIKE :status)" +
            "AND (:species IS NULL OR species LIKE :species)"+
            "AND (:type IS NULL OR type LIKE :type)" +
            "AND (:gender IS NULL OR type LIKE :gender)")
    fun getFilteredCharacters(name: String?, status: String?, species: String?, type: String?, gender: String?): List<CharacterDB>

    @Query("SELECT * FROM characters")
    fun getAllCharacters(): List<CharacterDB>

    @Query("SELECT * FROM characters WHERE id = :id")
    fun getCharacterById(id: Int): CharacterDB

}