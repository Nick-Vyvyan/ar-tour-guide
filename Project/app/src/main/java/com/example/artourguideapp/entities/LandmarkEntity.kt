package com.example.artourguideapp.entities

import android.location.Location
import com.google.android.gms.maps.model.LatLng

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
    latLng: LatLng,
    landmarkData: LandmarkData,
    searchId: Int,
) : Entity(
    landmarkData.getTitle(),
    landmarkData.getURL(),
    latLng,
    landmarkData,
    searchId) {

    init {
        setDialogFragment(LandmarkDialogFragment(this))
    }
}