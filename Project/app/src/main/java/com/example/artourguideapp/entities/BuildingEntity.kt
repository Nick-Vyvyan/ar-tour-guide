package com.example.artourguideapp.entities

import android.location.Location
import com.google.android.gms.maps.model.LatLng

/**
 * A subclass of [Entity]. The main container class for all building information.
 *
 * It contains a [BuildingDialogFragment] that displays information about this object.
 * @constructor Create a BuildingEntity by providing a location, building data, and search ID
 *
 * @param latLng Building latitude and longitude
 * @param buildingData Building data
 * @param searchId Search ID
 */
class BuildingEntity(
    latLng: LatLng,
    buildingData: BuildingData,
    searchId: Int,
) : Entity(
    buildingData.getTitle(),
    buildingData.getURL(),
    latLng,
    buildingData,
    searchId) {

    init {
        setDialogFragment(BuildingDialogFragment(this))
    }

}