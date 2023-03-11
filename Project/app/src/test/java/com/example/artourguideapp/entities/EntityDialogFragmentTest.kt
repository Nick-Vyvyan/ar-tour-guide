package com.example.artourguideapp.entities

import com.google.android.gms.maps.model.LatLng
import com.google.common.truth.Truth.assertThat

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class EntityDialogFragmentTest {

    private lateinit var testBuildingEntity: BuildingEntity
    private lateinit var testBuildingData: BuildingData
    private var testBuildingLatLng = LatLng(20.0, 40.0)

    private lateinit var testBuildingEntity2: BuildingEntity
    private lateinit var testBuildingData2: BuildingData
    private var testBuildingLatLng2 = LatLng(20.0, 40.0)

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

        // Set up BuildingEntity
        testBuildingData2 = BuildingData(
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

        testBuildingEntity2 = BuildingEntity(testBuildingLatLng2, testBuildingData2, 0)
    }

    @Test
    fun getEntity() {
        val result = testBuildingEntity.getDialogFragment().entity == testBuildingEntity
        assertThat(result).isTrue()
    }

    @Test
    fun setEntity() {
        testBuildingEntity.getDialogFragment().entity = testBuildingEntity2

        val result = testBuildingEntity.getDialogFragment().entity == testBuildingEntity2
        assertThat(result).isTrue()
    }
}