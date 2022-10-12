package com.example.artourguideapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity

class SensorActivity : Activity(), SensorEventListener, Publisher {

    override lateinit var subscribers: ArrayList<Subscriber>

    private var sensorManager : SensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var accelerometer : Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private var magnetometer : Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

    private val accelerometerReading : FloatArray = FloatArray(3)
    private val magnetometerReading : FloatArray = FloatArray(3)

    public fun startUpdates() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    init {
        startUpdates()
        subscribers = ArrayList()
    }

    public fun stopUpdates() {
        sensorManager.unregisterListener(this)
    }

    public fun getAccelerometer() : FloatArray {
        return accelerometerReading
    }

    public fun getMagnetometer() : FloatArray {
        return magnetometerReading
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event != null) {
            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                System.arraycopy(event.values, 0, accelerometerReading, 0, accelerometerReading.size)
                updateSubscribers()
            }
            else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
                System.arraycopy(event.values, 0, magnetometerReading, 0, magnetometerReading.size)
                updateSubscribers()
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        // do nothing
    }

    override fun register(subscriber: Subscriber): Subscriber {
        subscribers.add(subscriber)
        return subscriber
    }

    override fun remove(subscriber: Subscriber): Subscriber {
        subscribers.remove(subscriber)
        return subscriber
    }

    override fun updateSubscribers() {
        for (sub in subscribers) {
            sub.update()
        }
    }


}