package com.example.artourguideapp.entities

class BuildingData( title: String,
                    private val code: String,
                    private val types: String,
                    private val departments: String,
                    private val accessibilityInfo: String,
                    private val genderNeutralRestrooms: String,
                    private val computerLabs: String,
                    private val dining: String,
                    private val parkingInfo: String,
                    url: String): EntityData(title, url) {



    fun getCode(): String {
        return code
    }

    fun getTypes(): String {
        return types
    }

    fun getDepartments(): String {
        return departments
    }

    fun getAccessibilityInfo(): String {
        return accessibilityInfo
    }

    fun getGenderNeutralRestrooms(): String {
        return genderNeutralRestrooms
    }

    fun getComputerLabs(): String {
        return computerLabs
    }

    fun getDining(): String {
        return dining
    }

    fun getParkingInfo(): String {
        return parkingInfo
    }


    override fun toString(): String {
        return ("title: " + getTitle()
                + "\ncode: " + code
                + "\ntypes: " + types
                + "\ndepartments: " + departments
                + "\naccessibilityInfo: " + accessibilityInfo
                + "\ngenderNeutralRestrooms: " + genderNeutralRestrooms
                + "\ncomputerLabs: " + computerLabs
                + "\nparkingInfo: " + parkingInfo
                + "\nurl: " + getURL())
    }
}