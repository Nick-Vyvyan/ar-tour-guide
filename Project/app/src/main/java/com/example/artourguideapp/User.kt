package com.example.artourguideapp

import android.content.Context
import android.location.Location

class User(
    context: Context
) {
    private var orientation = Orientation(context)
    private var userLocation = UserLocation(context)
    private lateinit var entitiesInView : List<Entity>

    fun getOrientation(): Orientation {
        return orientation
    }

    fun getLocation() : Location {
        return userLocation.getLocation()
    }

    fun getInView() {

    }


    fun hasViewedAllNearbyBuildings() {
        TODO("Not yet implemented")
    }
}