package com.example.artourguideapp.entities

import android.location.Location
import androidx.fragment.app.DialogFragment

class LandmarkEntity(
    name: String,
    perimeter: ArrayList<Location>,
    location : Location,
    landmarkData: LandmarkData
) : Entity(
    name,
    perimeter,
    landmarkData.getURL(),
    location,
    landmarkData,
    LandmarkDataDialogFragment(landmarkData),
    null
) {

}