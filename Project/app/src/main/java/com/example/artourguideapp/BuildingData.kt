package com.example.artourguideapp

class BuildingData( private val title: String?,
                    private val code: String?,
                    private val types: String?,
                    private val departments: String?,
                    private val accessibilityInfo: String?,
                    private val genderNeutralRestrooms: String?,
                    private val computerLabs: String?,
                    private val dining: String?,
                    private val parkingInfo: String?,
                    private val url: String?) {


    fun getTitle(): String? {
        return title
    }

    fun getCode(): String? {
        return code
    }

    fun getTypes(): String? {
        return types
    }

    fun getDepartments(): String? {
        return departments
    }

    fun getAccessibilityInfo(): String? {
        return accessibilityInfo
    }

    fun getGenderNeutralRestrooms(): String? {
        return genderNeutralRestrooms
    }

    fun getComputerLabs(): String? {
        return computerLabs
    }

    fun getDining(): String? {
        return dining
    }

    fun getParkingInfo(): String? {
        return parkingInfo
    }

    fun getURL(): String? {
        return url
    }




}