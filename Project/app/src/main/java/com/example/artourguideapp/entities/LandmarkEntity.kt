package com.example.artourguideapp.entities

import com.google.android.gms.maps.model.LatLng

/**
 * A subclass of [Entity]. The main container class for all landmark information.
 *
 * It contains a [LandmarkDialogFragment] that displays information about this object.
 * @constructor Create a LandmarkEntity by providing a location, landmark data, and search ID
 *
 * @param latLng Landmark latitude and longitude
 * @param landmarkData Landmark data
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