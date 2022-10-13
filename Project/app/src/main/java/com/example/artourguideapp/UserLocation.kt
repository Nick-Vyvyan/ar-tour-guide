package com.example.artourguideapp

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*


private const val DEFAULT_UPDATE_INTERVAL : Long= 15
private const val FAST_UPDATE_INTERVAL : Long = 5
private const val PERMISSIONS_FINE_LOCATION = 101

class UserLocation(
    private val context: Context
){
    private lateinit var currentLocation : Location
    private var locationRequest: LocationRequest = LocationRequest()
    private var locationCallback: LocationCallback
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private var onLocationUpdate: ((LocationResult) -> Unit)? = null

    init {
        // config location request
        locationRequest.interval = 1000 * DEFAULT_UPDATE_INTERVAL
        locationRequest.fastestInterval = 1000 * FAST_UPDATE_INTERVAL
        locationRequest.priority = Priority.PRIORITY_HIGH_ACCURACY

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                super.onLocationResult(result)
                onLocationUpdate?.invoke(result)
                currentLocation = result.lastLocation!!
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
1        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }



    /**WILL BE NEEDED IN (MAIN ACTIVITY??)**/
//    fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//
//        when (requestCode) {
//            PERMISSIONS_FINE_LOCATION -> {
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    updateGPS()
//                } else {
//                    Toast.makeText(this, "This app requires location permissions to function properly", Toast.LENGTH_SHORT).show()
//                    finish()
//                }
//            }
//        }
//    }


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