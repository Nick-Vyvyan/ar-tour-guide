package com.example.artourguideapp.entities

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class LandmarkDataTest {

    private lateinit var testLandmarkData: LandmarkData

    @Before
    fun setUp() {
        testLandmarkData = LandmarkData(
            "Stairs to Nowhere",
            "tellus cras adipiscing enim eu turpis egestas pretium aenean pharetra magna ac placerat vestibulum lectus",
            "audio file name",
            "https://westerngallery.wwu.edu/mark-di-suvero-handel-1975"
        )
    }

    @Test
    fun getDescription() {
        val result = testLandmarkData.getDescription() == "tellus cras adipiscing enim eu turpis egestas pretium aenean pharetra magna ac placerat vestibulum lectus"
        assertThat(result).isEqualTo(true)
    }

    @Test
    fun testToString() {
        var expectedText = ("title: Stairs to Nowhere"
                + "\ndescription: tellus cras adipiscing enim eu turpis egestas pretium aenean pharetra magna ac placerat vestibulum lectus"
                + "\naudioFileName: audio file name"
                + "\nurl: https://westerngallery.wwu.edu/mark-di-suvero-handel-1975")
        val result = testLandmarkData.toString() == expectedText
        assertThat(result).isEqualTo(true)
    }
}