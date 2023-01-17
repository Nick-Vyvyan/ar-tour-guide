package com.example.artourguideapp.entities

import android.graphics.PointF
import android.location.Location

/**
 * A LandmarkEntity is a subclass of Entity and is constructed with a name, center,
 * location, and a LandmarkData object.
 *
 * It contains a LandmarkInfoDialogFragment that can be retrieved and shown in whatever
 * activity is desired.
 */
class LandmarkEntity(
    location : Location,
    landmarkData: LandmarkData
) : Entity(
    landmarkData.getTitle(),
    landmarkData.getURL(),
    location,
    landmarkData) {

    init {
        setDialogFragment(LandmarkDialogFragment(landmarkData, location, this))
    }
}