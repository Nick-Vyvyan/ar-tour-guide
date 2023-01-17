package com.example.artourguideapp.entities

import android.graphics.PointF
import android.location.Location

/**
 * A BuildingEntity is a subclass of Entity and is constructed with a name, center,
 * location, and a BuildingData object.
 *
 * It contains a BuildingInfoDialogFragment that can be retrieved and shown in whatever
 * activity is desired.
 */
class BuildingEntity(
    location : Location,
    buildingData: BuildingData
) : Entity(
    buildingData.getTitle(),
    buildingData.getURL(),
    location,
    buildingData) {

    init {
        setDialogFragment(BuildingDataDialogFragment(buildingData, location, this))
    }

}