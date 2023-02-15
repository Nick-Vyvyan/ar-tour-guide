package com.example.artourguideapp.entities

/**
 * A class that represents the data of a [LandmarkEntity].
 *
 * Data is displayed to the user through a [BuildingDialogFragment].
 *
 * This class is a subclass of [EntityData].
 *
 * @constructor Create a LandmarkData object
 *
 * @param title Landmark title
 * @param description Landmark description
 * @param audioFileName Landmark audio file name
 * @param url Landmark url
 */
class LandmarkData(title: String,
                   private var description: String,
                   audioFileName: String,
                   url: String): EntityData(title, audioFileName, url) {

    init {
        description = formatArrayString(description)
    }

    /**
     * Get description
     *
     * @return Landmark description
     */
    fun getDescription(): String {
        return description
    }

    /**
     * Returns a formatted String representing this [LandmarkData] object
     *
     * @return String representation of this [LandmarkData]
     */
    override fun toString(): String {
        return ("title: " + getTitle()
                + "\ndescription: " + description
                + "\naudioFileName: " + getAudioFileName()
                + "\nurl: " + getURL())
    }
}