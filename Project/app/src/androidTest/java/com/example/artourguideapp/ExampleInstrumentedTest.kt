package com.example.artourguideapp

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    private lateinit var appContext : Context
    @Before
    fun createAppContext() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.artourguideapp", appContext.packageName)
    }

    @Test
    fun createAccelerometerSensor() {
        val accelerometer : MeasurableSensor = Accelerometer(appContext)
        assertTrue(accelerometer.doesSensorExist)
    }

    @Test
    fun createMagnetometerSensor() {
        val magnetometer : MeasurableSensor = Magnetometer(appContext)
        assertTrue(magnetometer.doesSensorExist)
    }

    @Test
    fun getOrientation() {
        var orientation : Orientation = Orientation(appContext)
        var array = orientation.getOrientation()
        orientation.stopUpdates()
        assertTrue(array.isNotEmpty())
    }



}