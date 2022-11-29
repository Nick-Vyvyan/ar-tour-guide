package com.example.artourguideapp.entities

import android.graphics.PointF
import android.location.Location
import androidx.fragment.app.DialogFragment

/**
 * A BuildingEntity is a subclass of Entity and is constructed with a name, center,
 * location, and a BuildingData object.
 *
 * It contains a BuildingInfoDialogFragment that can be retrieved and shown in whatever
 * activity is desired.
 */
class BuildingEntity(
    name: String,
    center: PointF,
    location : Location,
    buildingData: BuildingData
) : Entity(
    name,
    center,
    buildingData.getURL(),
    location,
    buildingData,
    BuildingDataDialogFragment(buildingData)) {
}