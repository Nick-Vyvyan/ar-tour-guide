package com.example.artourguideapp.entities

import android.graphics.PointF
import android.location.Location

class LandmarkEntity(
    name: String,
    center: PointF,
    location : Location,
    landmarkData: LandmarkData
) : Entity(
    name,
    center,
    landmarkData.getURL(),
    location,
    landmarkData,
    LandmarkDataDialogFragment(landmarkData)
) {

}