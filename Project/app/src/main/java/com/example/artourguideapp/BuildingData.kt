package com.example.artourguideapp

class BuildingData( private val title: String?,
                    private val code: String?,
                    private val types: String?,
                    private val departments: String?,
                    private val accessibilityInfo: String?,
                    private val genderNeutralRestrooms: String?,
                    private val computerLabs: String?,
                    private val audioFileName: String?,
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

    fun getAudioFileName(): String? {
        return audioFileName
    }

    fun getComputerLabs(): String? {
        return computerLabs
    }

    fun getURL(): String? {
        return url
    }

    override fun toString(): String {
        return ("title: " + title
                + "\ncode: " + code
                + "\ntypes: " + types
                + "\ndepartments: " + departments
                + "\naccessibilityInfo: " + accessibilityInfo
                + "\ngenderNeutralRestrooms: " + genderNeutralRestrooms
                + "\ncomputerLabs: " + computerLabs
                + "\naudioFileName: " + audioFileName
                + "\nurl: " + url)
    }
}