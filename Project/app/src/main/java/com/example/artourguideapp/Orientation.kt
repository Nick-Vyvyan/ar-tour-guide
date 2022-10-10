package com.example.artourguideapp

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.hardware.SensorManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.location.Location
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.getSystemService
import androidx.core.app.ActivityCompat.requestPermissions
import com.google.android.gms.location.*

private const val PERMISSIONS_FINE_LOCATION = 101

class Orientation : SensorEventListener {
    private var heading = FloatArray(3)
    private var location = Location("")


    // values used for compass calculation
    lateinit var sensorManager: SensorManager
    private val accelerometerReading = FloatArray(3)
    private val magnetometerReading = FloatArray(3)
    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)

    // Location Request is config file for settings of FusedLocationProviderClient
    lateinit var locationRequest : LocationRequest

    // event when location update interval is met
    var locationCallBack = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)

//            updateUIValues(p0.lastLocation!!)
            setLocation(p0.lastLocation!!)
        }
    }

    // Google API for location services
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    fun getRotation() : FloatArray {
        return heading
    }

    fun getLocation() : Location {
        return location
    }

    private fun setLocation(location: Location) : Int
    {
        this.location = location
        return 0
    }

    private fun updateLocation(activity: Activity)
    {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)

        if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // user has provided permissions
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location : Location? ->
                if (location != null) {
                    setLocation(location)
                }
            }
        }
        else {
            // permissions have not been granted

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSIONS_FINE_LOCATION)
            }
        }
    }

    private fun updateHeading(activity: Activity)
    {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {

            // get readings from accelerometer and magnetometer. store as unit vectors??
            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                System.arraycopy(event.values, 0, accelerometerReading, 0, accelerometerReading.size)
            }
            else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
                System.arraycopy(event.values, 0, magnetometerReading, 0, magnetometerReading.size)
            }

            // update rotation matrix and orientation angles with SensorManager
            updateOrientationAngles()

            // update compass UI
            var degree : Float = Math.toDegrees(orientationAngles[0].toDouble()).toFloat()
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        TODO("Not yet implemented")
    }

    private fun updateOrientationAngles() {
        SensorManager.getRotationMatrix(
            rotationMatrix,
            null,
            accelerometerReading,
            magnetometerReading
        )

        SensorManager.getOrientation(rotationMatrix, orientationAngles)
    }
}