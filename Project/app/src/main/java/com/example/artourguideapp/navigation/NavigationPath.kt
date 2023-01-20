package com.example.artourguideapp.navigation

import android.location.Location

data class NavigationPath(val waypoints: MutableList<Location>, val distanceInMeters: Float)