package com.example.artourguideapp.entities

/**
 * An abstract class which is used as the template for BuildingData and LandmarkData objects,
 * so that they can be stored and displayed together along with their Entity counterparts.
 */
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