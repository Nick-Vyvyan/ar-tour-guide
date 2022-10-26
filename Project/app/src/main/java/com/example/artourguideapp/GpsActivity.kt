package com.example.artourguideapp

import android.content.pm.PackageManager
import android.hardware.SensorManager
import android.location.Location
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.allViews
import androidx.recyclerview.widget.RecyclerView


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

    lateinit var tv_nearby : TextView
    lateinit var tv_looking : TextView
    lateinit var ll_interactable: LinearLayout

    // COMPASS
    private val accelerometerReading = FloatArray(3)
    private val magnetometerReading = FloatArray(3)
    private val accelerometer : MeasurableSensor = Accelerometer(this)
    private val magnetometer : MeasurableSensor = Magnetometer(this)
    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)

    private lateinit var userLocation : UserLocation

    private lateinit var user: User


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

        tv_looking = findViewById(R.id.tv_lookingat)
        tv_nearby = findViewById(R.id.tv_nearby)
        ll_interactable = findViewById(R.id.interactable_buildings)

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

        user = User(this)

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

        val nearbyList : ArrayList<Entity> = user.detectNearbyEntities()
        var nearby = ""
        nearbyList.forEach{
            nearby += it.getName() + "\n"
        }
        tv_nearby.text = nearby

        val lookingAtList = user.getInView()
        var lookingAt = ""
        ll_interactable.removeAllViews()
        lookingAtList.forEach {
            val name = it.getName()
            lookingAt += name + "\n"
            println("added $name to looking at list")
            var button: Button = Button(this)
            var dialogFragment = it.getDialogFragment()
            button.text = it.getName()
            button.setOnClickListener() {
                dialogFragment.show(supportFragmentManager, name)
            }

            ll_interactable.addView(button)
        }
        tv_looking.text = lookingAt
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
        val ra : RotateAnimation = RotateAnimation(
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