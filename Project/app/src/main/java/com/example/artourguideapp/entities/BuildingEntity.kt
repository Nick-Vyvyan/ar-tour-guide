package com.example.artourguideapp.entities

import android.location.Location
import androidx.fragment.app.DialogFragment

/**
 * A BuildingEntity is a subclass of Entity and is constructed with a name, perimeter,
 * location, and a BuildingData object
 *
 * It contains a BuildingInfoDialogFragment that can be retrieved and shown in whatever
 * activity is desired
 */
class BuildingEntity(
    name: String,
    perimeter: ArrayList<Location>,
    location : Location,
    buildingData: BuildingData
) : Entity(
    name,
    perimeter,
    buildingData.getURL(),
    location,
    buildingData,
    BuildingDataDialogFragment(buildingData)) {
}