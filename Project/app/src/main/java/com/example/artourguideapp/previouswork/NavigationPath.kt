package com.example.artourguideapp.previouswork

import android.location.Location

data class NavigationPath(val waypoints: MutableList<Location>, val distanceInMeters: Float)