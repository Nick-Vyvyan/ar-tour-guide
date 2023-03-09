package com.example.artourguideapp.entities

import com.google.common.truth.Truth.assertThat

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class EntityDataTest {

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
    fun getTitle() {
        val result = testLandmarkData.getTitle() == "Stairs to Nowhere"
        assertThat(result).isEqualTo(true)
    }

    @Test
    fun getAudioFileName() {
        val result = testLandmarkData.getAudioFileName() == "audio file name"
        assertThat(result).isEqualTo(true)
    }

    @Test
    fun getURL() {
        val result = testLandmarkData.getURL() == "https://westerngallery.wwu.edu/mark-di-suvero-handel-1975"
        assertThat(result).isEqualTo(true)
    }

    @Test
    fun formatArrayString() {
        // Description = "
        testLandmarkData = LandmarkData(
            "Stairs to Nowhere",
            "[\"this\",\"is\",\"on\",\"different\",\"lines\"]",
            "audio file name",
            "https://westerngallery.wwu.edu/mark-di-suvero-handel-1975"
        )

        val result = testLandmarkData.getDescription() == "this\nis\non\ndifferent\nlines"
        assertThat(result).isEqualTo(true)
    }
}