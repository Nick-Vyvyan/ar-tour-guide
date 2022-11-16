package com.example.artourguideapp

class SculptureData(private val title: String?,
                    private val description: String?,
                    private val audioFileName: String?,
                    private val url: String?) {

    fun getTitle(): String? {
        return title
    }

    fun getDescription(): String? {
        return description
    }

    fun getAudioFileName(): String? {
        return audioFileName
    }

    fun getURL(): String? {
        return url
    }

    override fun toString(): String {
        return ("title: " + title
                + "\ndescription: " + description
                + "\naudioFileName: " + audioFileName
                + "\nurl: " + url)
    }
}