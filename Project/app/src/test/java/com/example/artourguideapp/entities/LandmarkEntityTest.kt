package com.example.artourguideapp.entities

import com.google.android.gms.maps.model.LatLng
import com.google.common.truth.Truth.assertThat

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class LandmarkEntityTest {

    // A test for code coverage of constructor
    @Test
    fun createLandmarkEntity() {
        var testLandmarkData = LandmarkData(
            "Stairs to Nowhere",
            "tellus cras adipiscing enim eu turpis egestas pretium aenean pharetra magna ac placerat vestibulum lectus",
            "audio file name",
            "https://westerngallery.wwu.edu/mark-di-suvero-handel-1975"
        )

        var testLandmarkEntity = LandmarkEntity(LatLng(20.0, 40.0), testLandmarkData, 0)
        val result = testLandmarkEntity.getName() != ""
        assertThat(result).isEqualTo(true)
    }
}