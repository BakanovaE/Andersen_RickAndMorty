package org

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.TestCase
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.martellina.rickandmorty.data.Database
import org.martellina.rickandmorty.data.dao.LocationDao
import org.martellina.rickandmorty.data.entity.LocationDB

class DatabaseTestLocation: TestCase() {

    private lateinit var database: Database
    private lateinit var locationDao: LocationDao

    @BeforeClass
    public override fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().context
        database = Room.inMemoryDatabaseBuilder(context, Database::class.java).build()
        locationDao = database.getLocationDao()
    }

    @AfterClass
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun testSaveLocation () {
        val location1 = LocationDB(1, "Earth", "", "", emptyList())
        val location2 = LocationDB(1, "NotEarth", "", "", emptyList())

        locationDao.run {
            saveLocations(location1)
            saveLocations(location2)
        }

        val locationFromDB = locationDao.getLocationById(1)

        assertEquals(location2.name, locationFromDB.name)
    }

    @Test
    fun testGetLocationByIdFromDB() {
        val testId = 1
        val locationDB = LocationDB(testId, "Earth", "", "", emptyList())
        val location2 = LocationDB(2, "NotEarth", "", "", emptyList())

        locationDao.run {
            saveLocations(locationDB)
            saveLocations(location2)
        }

        val result = locationDao.getLocationById(testId)

        assertEquals(locationDB.name, result.name)
    }
}