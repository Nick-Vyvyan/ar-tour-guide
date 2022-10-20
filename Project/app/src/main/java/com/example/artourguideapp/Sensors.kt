package com.example.artourguideapp

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor

class Accelerometer(
    context : Context
): AndroidSensor(
    context = context,
    sensorFeature = PackageManager.FEATURE_SENSOR_ACCELEROMETER,
    sensorType = Sensor.TYPE_ACCELEROMETER
)

class Magnetometer(
    context: Context
): AndroidSensor(
    context = context,
    sensorFeature = PackageManager.FEATURE_SENSOR_COMPASS,
    sensorType = Sensor.TYPE_MAGNETIC_FIELD
)