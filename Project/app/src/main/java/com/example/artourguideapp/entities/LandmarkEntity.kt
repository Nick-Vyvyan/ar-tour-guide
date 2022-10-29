package com.example.artourguideapp.entities

import android.location.Location
import androidx.fragment.app.DialogFragment

class LandmarkEntity(
    name: String,
    perimeter: ArrayList<Location>,
    location : Location,
    private var landmarkData: LandmarkData
): Entity(name, perimeter, landmarkData.getURL(), location) {

    private var landmarkDataDialogFragment: LandmarkDataDialogFragment = LandmarkDataDialogFragment(landmarkData)

    fun getLandmarkData(): LandmarkData {
        return landmarkData
    }

    override fun getDialogFragment(): DialogFragment {
        return landmarkDataDialogFragment
    }



}