package com.example.artourguideapp.entities

/**
 * A class that represents the data of a LandmarkEntity, which is shown in the structure popup dialog.
 *
 * This class is a subclass of EntityData.
 */
class LandmarkData(title: String,
                   private var description: String,
                   audioFileName: String,
                   url: String): EntityData(title, audioFileName, url) {

    init {
        description = formatArrayString(description)
    }

    fun getDescription(): String {
        return description
    }

    override fun toString(): String {
        return ("title: " + getTitle()
                + "\ndescription: " + description
                + "\naudioFileName: " + getAudioFileName()
                + "\nurl: " + getURL())
    }
}