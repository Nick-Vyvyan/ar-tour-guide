package com.example.artourguideapp

import android.location.Location
import com.example.artourguideapp.entities.BuildingData
import com.example.artourguideapp.entities.BuildingEntity
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class EntityUnitTests {
    lateinit var buildingEntity: BuildingEntity

    private var testBuildingData: BuildingData = BuildingData(
        "Name",
        "Code",
        "Types",
        "Departments",
        "Accessibility Info",
        "Gender Neutral Restrooms",
        "Computer Labs",
        "Dining",
        "",
        "URL"
    )

    var testLocation = Location("Test Location")

    @Before
    fun setup() {
        testLocation.latitude = 4.0
        testLocation.longitude = 20.0
        buildingEntity = BuildingEntity(testLocation, testBuildingData)
    }

    /**
     * Entity Unit Tests
     */
    @Test
    fun getName() {
        assertTrue(buildingEntity.getName() == "Test Entity")
    }

    @Test
    fun getURL() {
        assertTrue(buildingEntity.getURL() == "URL")
    }

    @Test
    fun getCentralLocation() {
        assertTrue(buildingEntity.getCentralLocation().latitude == 4.0)
        assertTrue(buildingEntity.getCentralLocation().longitude == 20.0)
    }

    @Test
    fun setLocation() {
        buildingEntity.setLocation(6.0, 9.0)
        assertTrue(buildingEntity.getCentralLocation().latitude == 6.0)
        assertTrue(buildingEntity.getCentralLocation().longitude == 9.0)
    }

    @Test
    fun getNode() {
        assertTrue(buildingEntity.getNode() == null)
    }

    @Test
    fun nodeIsAttached() {
        assertFalse(buildingEntity.nodeIsAttached())
    }

    @Test
    fun getEntityData() {
        assertTrue(buildingEntity.getEntityData() == testBuildingData)
    }

}