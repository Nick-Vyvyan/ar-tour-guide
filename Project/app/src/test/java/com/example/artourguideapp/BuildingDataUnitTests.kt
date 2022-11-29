package com.example.artourguideapp

import com.example.artourguideapp.entities.BuildingData
import org.junit.Assert.*
import org.junit.Test

class BuildingDataUnitTests {
    private val testBuildingData: BuildingData = BuildingData(
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

    @Test
    fun getTitle() {
        assertTrue(testBuildingData.getTitle() == "Name")
    }

    @Test
    fun getTypes() {
        assertTrue(testBuildingData.getTypes() == "Types")
    }

    @Test
    fun getDepartments() {
        assertTrue(testBuildingData.getDepartments() == "Departments")
    }

    @Test
    fun getAccessibilityInfo() {
        assertTrue(testBuildingData.getAccessibilityInfo() == "Accessibility Info")
    }

    @Test
    fun getGenderNeutralRestrooms() {
        assertTrue(testBuildingData.getGenderNeutralRestrooms() == "Gender Neutral Restrooms")
    }

    @Test
    fun getComputerLabs() {
        assertTrue(testBuildingData.getComputerLabs() == "Computer Labs")
    }

    @Test
    fun getDining() {
        assertTrue(testBuildingData.getDining() == "Dining")
    }

    @Test
    fun getURL() {
        assertTrue(testBuildingData.getURL() == "URL")
    }
}