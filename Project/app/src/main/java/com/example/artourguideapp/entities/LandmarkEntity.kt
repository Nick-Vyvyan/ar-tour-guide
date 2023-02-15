package com.example.artourguideapp.entities

import android.location.Location

/**
 * A subclass of [Entity]. The main container class for all landmark information.
 *
 * It contains a [LandmarkDialogFragment] that displays information about this object.
 * @constructor Create a LandmarkEntity by providing a location, landmark data, and search ID
 *
 * @param location Landmark location
 * @param buildingData Landmark data
 * @param searchId Search ID
 */
class LandmarkEntity(
    location : Location,
    landmarkData: LandmarkData,
    searchId: Int,
) : Entity(
    landmarkData.getTitle(),
    landmarkData.getURL(),
    location,
    landmarkData,
    searchId) {

    init {
        setDialogFragment(LandmarkDialogFragment(this))
    }
}