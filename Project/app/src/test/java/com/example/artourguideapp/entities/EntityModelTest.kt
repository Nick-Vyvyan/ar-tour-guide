package com.example.artourguideapp.entities

import com.example.artourguideapp.entities.*
import com.google.android.gms.maps.model.LatLng
import com.google.common.truth.Truth.assertThat

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class EntityModelTest {
    private lateinit var testBuildingEntity: BuildingEntity
    private lateinit var testBuildingData: BuildingData
    private var testBuildingLatLng = LatLng(20.0, 40.0)

    private lateinit var testLandmarkEntity: LandmarkEntity
    private lateinit var testLandmarkData: LandmarkData
    private var testLandmarkLatLng = LatLng(20.0, 40.0)

    private var entityList = mutableListOf<Entity>()

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

        testLandmarkData = LandmarkData(
            "Stairs to Nowhere",
            "tellus cras adipiscing enim eu turpis egestas pretium aenean pharetra magna ac placerat vestibulum lectus",
            "audio file name",
            "https://westerngallery.wwu.edu/mark-di-suvero-handel-1975"
        )
        testLandmarkEntity = LandmarkEntity(testLandmarkLatLng, testLandmarkData, 1)

        entityList.add(testBuildingEntity)
        entityList.add(testLandmarkEntity)
    }

    @Test
    fun getEntities() {
        EntityModel.setEntities(entityList)
        val result = EntityModel.getEntities().size > 0

        assertThat(result).isEqualTo(true)
    }

    @Test
    fun setEntities() {
        EntityModel.setEntities(DummyEntities.getEntityList())
        val result = EntityModel.getEntities().size > 0

        assertThat(result).isEqualTo(true)
    }

    @Test
    fun clearEntities() {
        EntityModel.setEntities(DummyEntities.getEntityList())
        var result = EntityModel.getEntities().size > 0

        assertThat(result).isEqualTo(true)

        EntityModel.clearEntities()
        result = EntityModel.getEntities().size == 0

        assertThat(result).isEqualTo(true)
    }

    @Test
    fun setSearchIndex() {
    }

    @Test
    fun getSearchIndex() {
    }
}