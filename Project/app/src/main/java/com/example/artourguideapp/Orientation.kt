package com.example.artourguideapp

import android.hardware.SensorManager

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