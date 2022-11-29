package com.example.artourguideapp.entities

/**
 * A class that represents the data of a LandmarkEntity, which is shown in the structure popup dialog.
 *
 * This class is a subclass of EntityData.
 */
class LandmarkData(title: String,
                   private val description: String,
                   private val audioDescription: String,
                   url: String): EntityData(title, url) {

    fun getDescription(): String {
        return description
    }

    fun getAudioDescription(): String {
        return audioDescription
    }

    override fun toString(): String {
        return ("title: " + getTitle()
                + "\ndescription: " + description
                + "\naudioDescription: " + audioDescription
                + "\nurl: " + getURL())
    }
}