package com.example.artourguideapp.entities

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