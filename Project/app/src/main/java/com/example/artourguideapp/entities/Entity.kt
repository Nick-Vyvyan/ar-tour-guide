package com.example.artourguideapp.entities

import android.location.Location
import androidx.fragment.app.DialogFragment
import com.google.ar.core.Anchor

abstract class Entity(
    private var name: String,
    private var perimeter: ArrayList<Location>,
    private var url: String,
    private var centralLocation : Location,
    private var entityData: EntityData,
    private var dialogFragment: DialogFragment,
    private var anchor: Anchor?
) {

    fun getName(): String {
        return name
    }

    fun getPerimeter(): ArrayList<Location> {
        return perimeter
    }

    fun setPerimeter(points: ArrayList<Location>) {
        perimeter = points
    }

    fun getURL(): String {
        return url
    }

    fun setAnchor(anchor: Anchor) {
        this.anchor = anchor
    }

    fun getAnchor() : Anchor? {
        return anchor
    }


    fun getCentralLocation() : Location
    {
        return centralLocation
    }

    fun setLocation(latitude : Double, longitude : Double) : Int
    {
        val latRange : ClosedRange<Double> = -90.0..90.0
        val lonRange : ClosedRange<Double> = -180.0..180.0
        if (latitude in latRange && longitude in lonRange)
        {
            centralLocation.latitude = latitude
            centralLocation.longitude = longitude
        }
        else
        {
            error("invalid latitude/longitude value in setLocation")
        }
        return 0

    }

    fun getDialogFragment(): DialogFragment {
        return dialogFragment
    }

    fun getEntityData(): EntityData {
        return entityData
    }

}