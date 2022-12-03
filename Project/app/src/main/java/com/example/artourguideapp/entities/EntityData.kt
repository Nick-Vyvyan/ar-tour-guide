package com.example.artourguideapp.entities

/**
 * An abstract class which is used as the template for BuildingData and LandmarkData objects,
 * so that they can be stored and displayed together along with their Entity counterparts.
 */
abstract class EntityData(
    private var title: String,
    private val audioFileName: String,
    private var url: String
) {
    fun getTitle(): String {
        return title
    }

    fun getAudioFileName(): String {
        return audioFileName
    }

    fun getURL(): String {
        return url
    }

    protected fun formatArrayString(arrayString: String) : String {
        return if (arrayString.length >= 2 && arrayString[0] == '[')
            arrayString.substring(1, arrayString.length - 1).replace("\",\"", "\n").replace("\"", "")
        else
            arrayString
    }
}