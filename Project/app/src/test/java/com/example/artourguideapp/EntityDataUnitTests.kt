package com.example.artourguideapp

import com.example.artourguideapp.entities.BuildingData
import com.example.artourguideapp.entities.LandmarkData
import org.junit.Assert.*
import org.junit.Test

class EntityDataUnitTests {
    private val testBuildingData: BuildingData = BuildingData(
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

    private val testLandmarkData: LandmarkData = LandmarkData("Name","Description","Audio File Name", "URL")

    /** ENTITY DATA TESTS */
    @Test
    fun getTitle() {
        assertTrue(testBuildingData.getTitle() == "Name")
        assertTrue(testLandmarkData.getTitle() == "Name")
    }

    @Test
    fun getAudioFileName() {
        assertTrue(testBuildingData.getAudioFileName() == "Audio File Name")
        assertTrue(testLandmarkData.getAudioFileName() == "Audio File Name")
    }

    @Test
    fun getURL() {
        assertTrue(testBuildingData.getURL() == "URL")
        assertTrue(testLandmarkData.getURL() == "URL")
    }

    /** BUILDING DATA TESTS */

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
    fun buildingToString() {
        assertTrue(testBuildingData.toString() == "title: Name"
                                                + "\ntypes: Types"
                                                + "\ndepartments: Departments"
                                                + "\naccessibilityInfo: Accessibility Info"
                                                + "\ngenderNeutralRestrooms: Gender Neutral Restrooms"
                                                + "\ncomputerLabs: Computer Labs"
                                                + "\ndining: Dining"
                                                + "\naudioFileName: Audio File Name"
                                                + "\nurl: URL")
    }

    /** LANDMARK DATA TESTS */
    @Test
    fun getDescription() {
        assertTrue(testLandmarkData.getDescription() == "Description")
    }

    @Test
    fun landmarkToString() {
        assertTrue(testLandmarkData.toString() == "title: Name"
                + "\ndescription: Description"
                + "\naudioFileName: Audio File Name"
                + "\nurl: URL")
    }


}