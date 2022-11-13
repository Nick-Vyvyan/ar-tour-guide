package com.example.artourguideapp

import java.io.File

class SculptureData(private val title: String?,
                    private val description: String?,
                    private val audioDescription: String?,
                    private val url: String?) {

    fun getTitle(): String? {
        return title
    }

    fun getDescription(): String? {
        return description
    }

    fun getAudioDescription(): String? {
        return audioDescription
    }

    fun getURL(): String? {
        return url
    }

}