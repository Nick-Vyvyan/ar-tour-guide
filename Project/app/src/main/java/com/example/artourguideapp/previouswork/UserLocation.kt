package com.example.artourguideapp.previouswork

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*


// private const val DEFAULT_UPDATE_INTERVAL : Long = 5
private const val FAST_UPDATE_INTERVAL : Long = 2
private const val PERMISSIONS_FINE_LOCATION = 101

class UserLocation(
    private val context: Context
){
    private var currentLocation : Location = Location("user")
    private var locationRequest: LocationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000 * FAST_UPDATE_INTERVAL).build()
    private var locationCallback: LocationCallback
    private var fusedLocationProviderClient: FusedLocationProviderClient

    private var onLocationUpdate: ((LocationResult) -> Unit)? = null

    init {
        // config location request
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                super.onLocationResult(result)
                currentLocation = result.lastLocation!!
                onLocationUpdate?.invoke(result)
            }
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

        startLocationUpdates()
    }

    fun getLocation() : Location {
        return currentLocation
    }

    fun setLocationUpdateListener(listener : (LocationResult) -> Unit) {
        onLocationUpdate = listener
    }

    fun startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
            // permissions not granted
           requestPermissions(context as Activity, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), PERMISSIONS_FINE_LOCATION)
        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private fun updateGPS() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location : Location? ->
                if (location != null) {
                    currentLocation = location
                }
            }
        } else { // fine location permission not granted
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(context as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSIONS_FINE_LOCATION)
            }
        }
    }


}