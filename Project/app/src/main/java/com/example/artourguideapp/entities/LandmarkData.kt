package com.example.artourguideapp.entities

/**
 * A class that represents the data of a LandmarkEntity, which is shown in the structure popup dialog.
 *
 * This class is a subclass of EntityData.
 */
class LandmarkData(title: String,
                   private val description: String,
                   private val audioFileName: String,
                   url: String): EntityData(title, url) {

    fun getDescription(): String {
        return description
    }

    fun getAudioFileName(): String {
        return audioFileName
    }

    override fun toString(): String {
        return ("title: " + getTitle()
                + "\ndescription: " + description
                + "\naudioDescription: " + audioFileName
                + "\nurl: " + getURL())
    }
}