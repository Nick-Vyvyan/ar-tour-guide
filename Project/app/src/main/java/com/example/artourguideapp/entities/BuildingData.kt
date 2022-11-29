package com.example.artourguideapp.entities

/**
 * A class that represents the data of a BuildingEntity, which is shown in the structure popup dialog.
 *
 * This class is a subclass of EntityData.
 */
class BuildingData(title: String,
                    private val types: String,
                    private val departments: String,
                    private val accessibilityInfo: String,
                    private val genderNeutralRestrooms: String,
                    private val computerLabs: String,
                    private val dining: String,
                    private val audioFileName: String,
                    url: String): EntityData(title, url) {
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

    fun getAudioFileName(): String {
        return audioFileName
    }

    override fun toString(): String {
        return ("title: " + getTitle()
                + "\ntypes: " + types
                + "\ndepartments: " + departments
                + "\naccessibilityInfo: " + accessibilityInfo
                + "\ngenderNeutralRestrooms: " + genderNeutralRestrooms
                + "\ncomputerLabs: " + computerLabs
                + "\ndining: " + dining
                + "\naudioFileName: " + audioFileName
                + "\nurl: " + getURL())
    }
}