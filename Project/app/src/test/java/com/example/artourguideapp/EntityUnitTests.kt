package com.example.artourguideapp

import android.location.Location
import com.example.artourguideapp.entities.BuildingData
import com.example.artourguideapp.entities.BuildingEntity
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class EntityUnitTests {

    lateinit var buildingEntity: BuildingEntity

    var testBuildingData: BuildingData = BuildingData(
        "Name",
        "Code",
        "Types",
        "Departments",
        "Accessibility Info",
        "Gender Neutral Restrooms",
        "Computer Labs",
        "Dining",
        "Parking Info",
        "URL"
    )

    var perimeter = ArrayList<Location>()
    var testPerimeterLocation = Location("Test Perimeter Location")

    var perimeter2 = ArrayList<Location>()
    var testPerimeterLocation2 = Location("Test Perimeter Location 2")

    var testLocation = Location("Test Location")


    @Before
    fun setup() {
        perimeter.clear()
        perimeter2.clear()
        perimeter.add(testPerimeterLocation)
        perimeter2.add(testPerimeterLocation2)
        testLocation.latitude = 4.0
        testLocation.longitude = 20.0
        buildingEntity = BuildingEntity("Test Entity", perimeter, testLocation, testBuildingData)
    }

    /**
     * Entity Unit Tests
     */
    @Test
    fun getName() {
        assertTrue(buildingEntity.getName() == "Test Entity")
    }

    @Test
    fun getPerimeter() {
        assertTrue(buildingEntity.getPerimeter() == perimeter)
    }

    @Test
    fun setPerimeter() {
        buildingEntity.setPerimeter(perimeter2)
        assertTrue(buildingEntity.getPerimeter() == perimeter2)
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