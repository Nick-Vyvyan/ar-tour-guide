package com.example.artourguideapp.entities


/**
 * An abstract class which is used as the template for [BuildingData] and [LandmarkData] objects,
 * so that they can be stored inside [Entity].
 *
 * @constructor Create an EntityData object
 *
 * @param title Entity title
 * @param audioFileName Entity audio file name
 * @param url Entity url
 */
abstract class EntityData(
    private var title: String,
    private val audioFileName: String,
    private var url: String
) {

    /**
     * Get entity title
     *
     * @return Entity title
     */
    fun getTitle(): String {
        return title
    }

    /**
     * Get entity audio file name
     *
     * @return Entity audio file name
     */
    fun getAudioFileName(): String {
        return audioFileName
    }

    /**
     * Get entity url
     *
     * @return Entity url
     */
    fun getURL(): String {
        return url
    }

    /**
     * Get a formatted string based upon the array string from the JSON. This function is used internally.
     *
     * @param arrayString the array String from a JSON file
     * @return The correctly formatted string
     */
    protected fun formatArrayString(arrayString: String) : String {
        return if (arrayString.length >= 2 && arrayString[0] == '[')
            arrayString.substring(1, arrayString.length - 1).replace("\",\"", "\n").replace("\"", "")
        else
            arrayString
    }
}