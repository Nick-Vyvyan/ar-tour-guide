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

class Orientation {

    private val sensorActivity : SensorActivity = SensorActivity()

    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)

    init {
        sensorActivity.startUpdates()
    }


    fun getOrientation() : FloatArray {

        calcOrientation()
        return orientationAngles
    }

    private fun calcOrientation() {
        val accelData : FloatArray = sensorActivity.getAccelerometer()
        val magData : FloatArray = sensorActivity.getMagnetometer()

        SensorManager.getRotationMatrix(
            rotationMatrix,
            null,
            accelData,
            magData
        )

        SensorManager.getOrientation(rotationMatrix, orientationAngles)
    }




}