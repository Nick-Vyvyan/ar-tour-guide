package com.example.artourguideapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Switch
import android.widget.TextView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.Priority

private const val DEFAULT_UPDATE_INTERVAL : Long = 30
private const val FAST_UPDATE_INTERVAL : Long = 5

class GpsActivity : AppCompatActivity() {

    // Text View UI Elements
    lateinit var tv_lat : TextView
    lateinit var tv_lon : TextView
    lateinit var tv_altitude : TextView
    lateinit var tv_accuracy : TextView
    lateinit var tv_speed : TextView
    lateinit var tv_sensor : TextView
    lateinit var tv_updates : TextView
    lateinit var tv_address : TextView

    // Switch UI Elements
    lateinit var sw_locationupdates : Switch
    lateinit var sw_gps : Switch

    var updateOn : Boolean = false

    // Location Request is config file for settings of FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest

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
        tv_address = findViewById(R.id.tv_address)

        sw_locationupdates = findViewById(R.id.sw_locationsupdates)
        sw_gps = findViewById(R.id.sw_gps)


        // set properties of location request
        locationRequest = LocationRequest()

        // set default location check frequency
        locationRequest.setInterval(1000 * DEFAULT_UPDATE_INTERVAL)
        //set fastest location check frequency
        locationRequest.setFastestInterval(1000 * FAST_UPDATE_INTERVAL)

        locationRequest.setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
    }









}