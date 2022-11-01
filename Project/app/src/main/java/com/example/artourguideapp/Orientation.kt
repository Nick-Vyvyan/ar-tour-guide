package com.example.artourguideapp

import android.content.Context
import android.hardware.SensorManager

class Orientation(
    context : Context
) {
    private val accelerometer : MeasurableSensor = Accelerometer(context)
    private val magnetometer : MeasurableSensor = Magnetometer(context)
    private val accelReading : FloatArray = FloatArray(3)
    private val magReading : FloatArray = FloatArray(3)
    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)

    init {
        accelerometer.setOnSensorValuesChangedListener { values ->
            System.arraycopy(values.toFloatArray(), 0, accelReading, 0, accelReading.size)
            updateOrientation()
        }

        magnetometer.setOnSensorValuesChangedListener { values ->
            System.arraycopy(values.toFloatArray(), 0, magReading, 0, magReading.size)
            updateOrientation()
        }

        accelerometer.startListening()
        magnetometer.startListening()
    }

    /** Returns Float Array of [Azimuth, Pitch, Roll]*/
    fun getOrientation() : FloatArray {
//        updateOrientation()
        return orientationAngles
    }

    fun startUpdates() {
        accelerometer.startListening()
        magnetometer.startListening()
    }

    fun stopUpdates() {
        accelerometer.stopListening()
        magnetometer.stopListening()
    }

    private fun updateOrientation() {
        SensorManager.getRotationMatrix(
            rotationMatrix,
            null,
            accelReading,
            magReading
        )

        SensorManager.getOrientation(rotationMatrix, orientationAngles)
    }

}