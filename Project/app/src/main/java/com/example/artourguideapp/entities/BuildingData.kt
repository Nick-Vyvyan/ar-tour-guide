package com.example.artourguideapp.entities

/**
 * A class that represents the data of a [BuildingEntity].
 *
 * Data is displayed to the user through a [BuildingDialogFragment].
 *
 * This class is a subclass of [EntityData].
 *
 * @constructor Create a BuildingData object
 *
 * @param title Building title
 * @param code Two letter building code
 * @param types Building types (i.e. Admissions, Residence)
 * @param departments Building departments (i.e. Humanities, Computer Science)
 * @param accessibilityInfo Building accessibility info
 * @param genderNeutralRestrooms Building gender neutral restroom locations
 * @param computerLabs Building computer lab locations
 * @param dining Building dining info
 * @param audioFileName Building audio file name
 * @param url Building url
 */
class BuildingData(title: String,
                    private var code: String,
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

    /**
     * Returns the building code
     *
     * @return Building code
     */
    fun getCode(): String {
        return code
    }

    /**
     * Returns the building types
     *
     * @return Building types
     */
    fun getTypes(): String {
        return types
    }

    /**
     * Returns the building departments
     *
     * @return Building departments
     */
    fun getDepartments(): String {
        return departments
    }

    /**
     * Returns the building accessibility info
     *
     * @return Building accessibility info
     */
    fun getAccessibilityInfo(): String {
        return accessibilityInfo
    }

    /**
     * Returns the building's gender neutral restrooms locations
     *
     * @return Building gender neutral restrooms locations
     */
    fun getGenderNeutralRestrooms(): String {
        return genderNeutralRestrooms
    }

    /**
     * Returns the building's computer lab locations
     *
     * @return Building computer lab locations
     */
    fun getComputerLabs(): String {
        return computerLabs
    }

    /**
     * Returns the building dining info
     *
     * @return Building dining info
     */
    fun getDining(): String {
        return dining
    }

    /**
     * Returns a formatted String representing this [BuildingData] object
     *
     * @return String representation of this [BuildingData]
     */
    override fun toString(): String {
        return ("title: " + getTitle()
                + "\ncode: " + code
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