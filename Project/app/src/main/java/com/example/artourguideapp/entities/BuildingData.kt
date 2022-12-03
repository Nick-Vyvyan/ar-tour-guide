package com.example.artourguideapp.entities

/**
 * A class that represents the data of a BuildingEntity, which is shown in the structure popup dialog.
 *
 * This class is a subclass of EntityData.
 */
class BuildingData(title: String,
                    private var types: String,
                    private var departments: String,
                    private var accessibilityInfo: String,
                    private var genderNeutralRestrooms: String,
                    private var computerLabs: String,
                    private var dining: String,
                    audioFileName: String,
                    url: String): EntityData(title, audioFileName, url) {

    init {
        types = formatArrayString(types)
        departments = formatArrayString(departments)
        accessibilityInfo = formatArrayString(accessibilityInfo)
        genderNeutralRestrooms = formatArrayString(genderNeutralRestrooms)
        computerLabs = formatArrayString(computerLabs)
        dining = formatArrayString(dining)
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

    override fun toString(): String {
        return ("title: " + getTitle()
                + "\ntypes: " + types
                + "\ndepartments: " + departments
                + "\naccessibilityInfo: " + accessibilityInfo
                + "\ngenderNeutralRestrooms: " + genderNeutralRestrooms
                + "\ncomputerLabs: " + computerLabs
                + "\ndining: " + dining
                + "\naudioFileName: " + getAudioFileName()
                + "\nurl: " + getURL())
    }
}