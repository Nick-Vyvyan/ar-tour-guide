package com.example.artourguideapp

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*


private const val DEFAULT_UPDATE_INTERVAL : Long = 30
private const val FAST_UPDATE_INTERVAL : Long = 5
private const val PERMISSIONS_FINE_LOCATION = 101

class GpsActivity : AppCompatActivity(), SensorEventListener {

    // Text View UI Elements
    lateinit var tv_lat : TextView
    lateinit var tv_lon : TextView
    lateinit var tv_altitude : TextView
    lateinit var tv_accuracy : TextView
    lateinit var tv_speed : TextView
    lateinit var tv_sensor : TextView
    lateinit var tv_updates : TextView
    lateinit var tv_heading : TextView

    // Switch UI Elements
    lateinit var sw_locationupdates : Switch
    lateinit var sw_gps : Switch

    var updateOn : Boolean = false

    // Used for compass
    lateinit var iv_compass : ImageView
    lateinit var sensorManager: SensorManager
    var degreeStart : Float = 0f

    // Location Request is config file for settings of FusedLocationProviderClient
    lateinit var locationRequest : LocationRequest

    lateinit var locationCallBack : LocationCallback

    // Google API for location services
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gps)

        //give UI variables values
        tv_lat = findViewById(R.id.tv_lat)
        tv_lon = findViewById(R.id.tv_lon)
        tv_altitude = findViewById(R.id.tv_altitude)
        tv_accuracy = findViewById(R.id.tv_accuracy)
        tv_speed = findViewById(R.id.tv_speed)
        tv_updates = findViewById(R.id.tv_updates)
        tv_sensor = findViewById(R.id.tv_sensor)
        tv_heading = findViewById(R.id.tv_heading)

        sw_locationupdates = findViewById(R.id.sw_locationsupdates)
        sw_gps = findViewById(R.id.sw_gps)

        iv_compass = findViewById(R.id.iv_compass)

        // initialize sensor manager
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        // set properties of location request
        locationRequest = LocationRequest()

        // set default location check frequency
        locationRequest.setInterval(1000 * DEFAULT_UPDATE_INTERVAL)
        //set fastest location check frequency
        locationRequest.setFastestInterval(1000 * FAST_UPDATE_INTERVAL)
        // set priority/accuracy (power) balance
        locationRequest.setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)

        // event when location update interval is met
        locationCallBack = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)

                updateUIValues(p0.lastLocation!!)
            }
        }

        sw_gps.setOnClickListener(View.OnClickListener {
            println("DEBUG - GPS TOGGLE CLICKED")
            if (sw_gps.isChecked) {
                //most accurate - use GPS
                locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                tv_sensor.text = "Using GPS sensors"
            }
            else {
                locationRequest.setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
                tv_sensor.text = "Cell Tower + Wifi"
            }
        })

        sw_locationupdates.setOnClickListener {
            println("DEBUG - LOCATION UPDATES CLICKED")
            if (sw_locationupdates.isChecked) {
                // turn on location updates
                startLocationUpdates()
            }
            else {
                // turn off tracking
                stopLocationUpdates()
            }
        }



        updateGPS()

    } // END onCreate method

    private fun startLocationUpdates() {
        tv_updates.text = "Location is being tracked"

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallBack, null)
    }


    private fun stopLocationUpdates() {
        tv_updates.text = "Location is NOT being tracked"

        //update UI elements
        tv_lat.text = "Not Tracking Location"
        tv_lon.text = "Not Tracking Location"
        tv_speed.text = "Not Tracking Location"
        tv_heading.text = "Not Tracking Location"
        tv_accuracy.text = "Not Tracking Location"
        tv_altitude.text = "Not Tracking Location"


        fusedLocationProviderClient.removeLocationUpdates(locationCallBack)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode) {
            PERMISSIONS_FINE_LOCATION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateGPS()
                }
                else {
                    Toast.makeText(this, "This app requires permission to be granted in order to work properly", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

        }
    }

    override fun onPause() {
        super.onPause()

        sensorManager.unregisterListener(this)
    }

    override fun onResume() {
        super.onResume()

        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME)
    }

    private fun updateGPS() {
        // get permissions from user
        // get current location from fused client
        // update UI elements

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // user has provided permissions
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location : Location? ->
                if (location != null) {
                    updateUIValues(location)
                }
            }
        }
        else {
            // permissions have not been granted

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSIONS_FINE_LOCATION)
            }
        }
    }

    private fun updateUIValues(location : Location) {
        // update all TextView with new location data
        tv_lat.text = location.latitude.toString()
        println("DEBUG - CURRENT LATITUDE = " + location.latitude.toString())
        tv_lon.text = location.longitude.toString()
        tv_accuracy.text = location.accuracy.toString()

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

    override fun onSensorChanged(p0: SensorEvent?) {
        if (p0 is SensorEvent) {
            // get roation data from sensor and convert to degrees
            var degree : Float = Math.toDegrees(p0.values[2].toDouble()).toFloat()

            tv_heading.text = degree.toString() + " degrees"

            // initialize a rotation animation
            var ra : RotateAnimation = RotateAnimation(
                degreeStart,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
            )
            // set animation rules
            ra.fillAfter = true
            ra.duration = 210

            // start animation and update starting rotation
            iv_compass.startAnimation(ra)
            degreeStart = -degree
        }


    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        // do nothing
    }


}