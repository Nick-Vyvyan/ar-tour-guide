package com.example.artourguideapp.entities

import com.google.common.truth.Truth.assertThat

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class BuildingDataTest {
    private lateinit var testBuildingData: BuildingData

    @Before
    fun setUp() {
        testBuildingData = BuildingData(
            "Name",
            "Code",
            "Types",
            "Departments",
            "Accessibility Info",
            "Gender Neutral Restrooms",
            "Computer Labs",
            "Dining",
            "Audio File Name",
            "URL"
        )
    }

    @Test
    fun getCode() {
        val result = testBuildingData.getCode() == "Code"
        assertThat(result).isEqualTo(true)
    }

    @Test
    fun getTypes() {
        val result = testBuildingData.getTypes() == "Types"
        assertThat(result).isEqualTo(true)
    }

    @Test
    fun getDepartments() {
        val result = testBuildingData.getDepartments() == "Departments"
        assertThat(result).isEqualTo(true)
    }

    @Test
    fun getAccessibilityInfo() {
        val result = testBuildingData.getAccessibilityInfo() == "Accessibility Info"
        assertThat(result).isEqualTo(true)
    }

    @Test
    fun getGenderNeutralRestrooms() {
        val result = testBuildingData.getGenderNeutralRestrooms() == "Gender Neutral Restrooms"
        assertThat(result).isEqualTo(true)
    }

    @Test
    fun getComputerLabs() {
        val result = testBuildingData.getComputerLabs() == "Computer Labs"
        assertThat(result).isEqualTo(true)
    }

    @Test
    fun getDining() {
        val result = testBuildingData.getDining() == "Dining"
        assertThat(result).isEqualTo(true)
    }

    @Test
    fun testToString() {
        val expectedText = ("title: Name"
        + "\ncode: Code"
        + "\ntypes: Types"
        + "\ndepartments: Departments"
        + "\naccessibilityInfo: Accessibility Info"
        + "\ngenderNeutralRestrooms: Gender Neutral Restrooms"
        + "\ncomputerLabs: Computer Labs"
        + "\ndining: Dining"
        + "\naudioFileName: Audio File Name"
        + "\nurl: URL")

        val result = testBuildingData.toString() == expectedText
        assertThat(result).isEqualTo(true)
    }
}