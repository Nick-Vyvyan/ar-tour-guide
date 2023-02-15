package com.example.artourguideapp.entities

import android.location.Location

/**
 * A subclass of [Entity]. The main container class for all building information.
 *
 * It contains a [BuildingDialogFragment] that displays information about this object.
 * @constructor Create a BuildingEntity by providing a location, building data, and search ID
 *
 * @param location Building location
 * @param buildingData Building data
 * @param searchId Search ID
 */
class BuildingEntity(
    location : Location,
    buildingData: BuildingData,
    searchId: Int,
) : Entity(
    buildingData.getTitle(),
    buildingData.getURL(),
    location,
    buildingData,
    searchId) {

    init {
        setDialogFragment(BuildingDialogFragment(this))
    }

}