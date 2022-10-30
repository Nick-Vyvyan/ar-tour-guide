package com.example.artourguideapp.entities

abstract class EntityData(
    private var title: String,
    private var url: String
) {
    fun getTitle(): String {
        return title
    }

    fun getURL(): String {
        return url
    }
}