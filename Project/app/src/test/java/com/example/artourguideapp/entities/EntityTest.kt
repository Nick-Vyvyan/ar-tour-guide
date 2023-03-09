package com.example.artourguideapp.entities

import android.location.Location
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class EntityTest {

    private lateinit var testBuildingEntity: BuildingEntity
    private lateinit var testBuildingData: BuildingData
    private lateinit var testBuildingLocation: Location

    @Before
    fun setUp() {

        // Set up BuildingEntity
        testBuildingData = BuildingData(
            "Name",
            "Code",
            "Types",
            "Departments",
            "Accessibility Info",
            "Gender Neutral Restrooms",
            "Computer Labs",
            "Dining",
            "Building Audio File Name",
            "URL"
        )

        testBuildingLocation = Location("Building")
        testBuildingLocation.latitude = 20.0
        testBuildingLocation.longitude = 40.0

        testBuildingEntity = BuildingEntity(testBuildingLocation, testBuildingData, 0)
    }

    @Test
    fun nodeIsAttached() {
        val result = testBuildingEntity.nodeIsAttached()
        assertThat(result).isEqualTo(false)
    }

    @Test
    fun getName() {
        val result = testBuildingEntity.getName() == "Name"
        assertThat(result).isEqualTo(true)
    }

    @Test
    fun getURL() {
        val result = testBuildingEntity.getURL() == "URL"
        assertThat(result).isEqualTo(true)
    }

    @Test
    fun getNode() {
        val result = testBuildingEntity.getNode() == null
        assertThat(result).isEqualTo(true)
    }

    @Test
    fun getSearchId() {
        val result = testBuildingEntity.getSearchId() == 0
        assertThat(result).isEqualTo(true)
    }

    @Test
    fun getCentralLocation() {
        val location = testBuildingEntity.getCentralLocation()
        val result = location.provider == "Building" &&
                     location.latitude == 20.0 &&
                     location.longitude == 40.0

        assertThat(result).isEqualTo(true)
    }

    @Test
    fun setLocation() {
        testBuildingEntity.setLocation(40.0, 20.0)

        val location = testBuildingEntity.getCentralLocation()
        val result = location.provider == "Building" &&
                     location.latitude == 40.0 &&
                     location.longitude == 20.0

        assertThat(result).isEqualTo(true)

        testBuildingEntity.setLocation(20.0, 40.0)
    }

    //TODO: Write UI testing for entity dialog fragments
//    @Test
//    fun getDialogFragment() {
//    }
//
//    @Test
//    fun setDialogFragment() {
//    }

    @Test
    fun getEntityData() {
        val result = testBuildingEntity.getEntityData() == testBuildingData
        assertThat(result).isEqualTo(true)
    }

    @Test
    fun setAsDestination() {
        var result = testBuildingEntity.isDestination()
        assertThat(result).isEqualTo(false)

        testBuildingEntity.setAsDestination()
        result = testBuildingEntity.isDestination()
        assertThat(result).isEqualTo(true)
    }

    @Test
    fun clearAsDestination() {
        var result = testBuildingEntity.isDestination()
        assertThat(result).isEqualTo(true)

        testBuildingEntity.clearAsDestination()
        result = testBuildingEntity.isDestination()
        assertThat(result).isEqualTo(false)
    }

    @Test
    fun isDestination() {
        var result = testBuildingEntity.isDestination()
        assertThat(result).isEqualTo(false)
    }
}