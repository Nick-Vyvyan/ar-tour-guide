package com.example.artourguideapp

import android.graphics.PointF
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
        "Types",
        "Departments",
        "Accessibility Info",
        "Gender Neutral Restrooms",
        "Computer Labs",
        "Dining",
        "Parking Info",
        "URL"
    )

    var center1 = PointF(4.0f, 20.0f)
    var center2 = PointF(5.0f, 30.0f)
    var testLocation = Location("Test Location")

    @Before
    fun setup() {
        testLocation.latitude = 4.0
        testLocation.longitude = 20.0
        buildingEntity = BuildingEntity("Test Entity", center1, testLocation, testBuildingData)
    }

    /**
     * Entity Unit Tests
     */
    @Test
    fun getName() {
        assertTrue(buildingEntity.getName() == "Test Entity")
    }

    @Test
    fun getCenter() {
        assertTrue(buildingEntity.getCenter() == center1)
    }

    @Test
    fun setCenter() {
        buildingEntity.setCenter(center2)
        assertTrue(buildingEntity.getCenter() == center2)
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
}