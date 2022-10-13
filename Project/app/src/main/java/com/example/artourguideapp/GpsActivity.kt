package com.example.artourguideapp

import android.content.pm.PackageManager
import android.hardware.SensorManager
import android.location.Location
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


private const val DEFAULT_UPDATE_INTERVAL : Long = 30
private const val FAST_UPDATE_INTERVAL : Long = 5
private const val PERMISSIONS_FINE_LOCATION = 101

class GpsActivity : AppCompatActivity() /*SensorEventListener*/ {

    // Text View UI Elements
    lateinit var tv_lat : TextView
    lateinit var tv_lon : TextView
    lateinit var tv_altitude : TextView
    lateinit var tv_accuracy : TextView
    lateinit var tv_speed : TextView
    lateinit var tv_sensor : TextView
    lateinit var tv_updates : TextView
    lateinit var tv_heading : TextView

    // Used for compass, store device orientation data
    lateinit var iv_compass : ImageView
    var degreeStart : Float = 0f

    // COMPASS
    private val accelerometerReading = FloatArray(3)
    private val magnetometerReading = FloatArray(3)
    private val accelerometer : MeasurableSensor = Accelerometer(this)
    private val magnetometer : MeasurableSensor = Magnetometer(this)
    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)

    private lateinit var userLocation : UserLocation


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gps)

        //initialize UI variables
        tv_lat = findViewById(R.id.tv_lat)
        tv_lon = findViewById(R.id.tv_lon)
        tv_altitude = findViewById(R.id.tv_altitude)
        tv_accuracy = findViewById(R.id.tv_accuracy)
        tv_speed = findViewById(R.id.tv_speed)
//        tv_updates = findViewById(R.id.tv_updates)
//        tv_sensor = findViewById(R.id.tv_sensor)
        tv_heading = findViewById(R.id.tv_heading)

        iv_compass = findViewById(R.id.iv_compass)

        // initialize sensor Listening
        accelerometer.startListening()
        accelerometer.setOnSensorValuesChangedListener { values ->
            System.arraycopy(values.toFloatArray(), 0, accelerometerReading, 0, accelerometerReading.size)
            updateOrientationAngles()
        }

        magnetometer.startListening()
        magnetometer.setOnSensorValuesChangedListener { values ->
            System.arraycopy(values.toFloatArray(), 0, magnetometerReading, 0, magnetometerReading.size)
            updateOrientationAngles()
        }

        userLocation = UserLocation(this)
        userLocation.startLocationUpdates()
        userLocation.setLocationUpdateListener { result ->
            updateUIValues(result.lastLocation!!)
        }

//        updateGPS()

    } // END onCreate method


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode) {
            PERMISSIONS_FINE_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    updateGPS()
                    println("DEBUG - FINE LOCATION PERMISSION GRANTED")
                }
                else {
                    Toast.makeText(this, "This app requires permission to be granted in order to work properly", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

        }
    }

    private fun updateUIValues(location : Location) {
        // update all TextView with new location data
        tv_lat.text = location.latitude.toString()
        println("DEBUG - CURRENT LATITUDE = " + location.latitude.toString())
        tv_lon.text = location.longitude.toString()
        tv_accuracy.text = location.accuracy.toString() + " meters"

        if (location.hasAltitude()) {
            tv_altitude.text = location.altitude.toString()
        }
        else {
            tv_altitude.text = "Not Available"
        }

        if (location.hasSpeed()) {
            tv_speed.text = location.speed.toString()
        }
        else {
            tv_altitude.text = "Not Available"
        }
    }

    private fun updateOrientationAngles() {
        SensorManager.getRotationMatrix(
            rotationMatrix,
            null,
            accelerometerReading,
            magnetometerReading
        )

        SensorManager.getOrientation(rotationMatrix, orientationAngles)
        updateCompassUI(Math.toDegrees(orientationAngles[0].toDouble()).toFloat())
    }

    private fun updateCompassUI(heading : Float) {
        tv_heading.text = heading.toString() + " degrees"

        // initialize a rotation animation
        var ra : RotateAnimation = RotateAnimation(
            degreeStart,
            -heading,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        // set animation rules
        ra.fillAfter = true
        ra.duration = 210

        // start animation and update starting rotation
        iv_compass.startAnimation(ra)
        degreeStart = -heading
    }


}