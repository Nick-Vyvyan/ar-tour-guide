package com.example.artourguideapp.entities

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class EntityTest {

    private lateinit var testBuildingEntity: BuildingEntity
    private lateinit var testBuildingData: BuildingData
    private var testBuildingLatLng = LatLng(20.0, 40.0)

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

        testBuildingEntity = BuildingEntity(testBuildingLatLng, testBuildingData, 0)
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
        val latLng = testBuildingEntity.getLatLng()
        val result = latLng.latitude == 20.0 &&
                latLng.longitude == 40.0

        assertThat(result).isEqualTo(true)
    }

    @Test
    fun setLocation() {
        testBuildingEntity.setLatLng(40.0, 20.0)

        val latLng = testBuildingEntity.getLatLng()
        val result = latLng.latitude == 40.0 &&
                latLng.longitude == 20.0

        assertThat(result).isEqualTo(true)

        assertThrows(IllegalStateException::class.java) {
            testBuildingEntity.setLatLng(-100.0, 20.0)
        }

        assertThrows(IllegalStateException::class.java) {
            testBuildingEntity.setLatLng(40.0, 220.0)
        }
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
        assertThat(result).isEqualTo(false)

        testBuildingEntity.setAsDestination()
        result = testBuildingEntity.isDestination()
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