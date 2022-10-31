/*
 * Copyright 2022 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/** This File has been changed since its original distribution*/

package com.example.artourguideapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.example.artourguideapp.arcore.java.com.google.ar.core.examples.java.common.helpers.FullScreenHelper
import com.example.artourguideapp.arcore.java.com.google.ar.core.examples.java.common.samplerender.SampleRender
import com.google.ar.core.Config
import com.google.ar.core.Session
import com.google.ar.core.codelabs.hellogeospatial.*
import com.google.ar.core.codelabs.hellogeospatial.helpers.*
import com.google.ar.core.exceptions.*
import com.google.ar.core.exceptions.UnavailableUserDeclinedInstallationException
import com.example.artourguideapp.entities.*
import kotlinx.coroutines.Runnable
import org.json.JSONArray
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.Timer
import java.util.TimerTask
import kotlin.concurrent.thread

class GeospatialActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "GeospatialActivity"
    }

    lateinit var arCoreSessionHelper: ARCoreSessionLifecycleHelper
    lateinit var view: HelloGeoView
    lateinit var renderer: HelloGeoRenderer


    var entityList : MutableList<Entity> = mutableListOf()
    val server: String = ""
    val model = Model()
    val userView = UserView()
    val user = null
    val controller = Controller(server, model, userView, user)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_geospatial)


        // Setup ARCore session lifecycle helper and configuration.
        arCoreSessionHelper = ARCoreSessionLifecycleHelper(this)
        // If Session creation or Session.resume() fails, display a message and log detailed
        // information.
        arCoreSessionHelper.exceptionCallback =
            { exception ->
                val message =
                    when (exception) {
                        is UnavailableUserDeclinedInstallationException ->
                            "Please install Google Play Services for AR"
                        is UnavailableApkTooOldException -> "Please update ARCore"
                        is UnavailableSdkTooOldException -> "Please update this app"
                        is UnavailableDeviceNotCompatibleException -> "This device does not support AR"
                        is CameraNotAvailableException -> "Camera not available. Try restarting the app."
                        else -> "Failed to create AR session: $exception"
                    }
                Log.e(GeospatialActivity.TAG, "ARCore threw an exception", exception)
                view.snackbarHelper.showError(this, message)
            }

        // Configure session features.
        arCoreSessionHelper.beforeSessionResume = ::configureSession
        lifecycle.addObserver(arCoreSessionHelper)

        // Set up the Hello AR renderer.
        renderer = HelloGeoRenderer(this)
        lifecycle.addObserver(renderer)

        // Set up Hello AR UI.
        view = HelloGeoView(this)
        lifecycle.addObserver(view)
        setContentView(view.root)

        SampleRender(view.surfaceView, renderer, assets)
//        renderer.setTestAnchors()


        DummyBuildingEntities.initialize()
        DummyBuildingEntities.entityList.forEach {
            this.entityList.add(it)
        }


        // update anchors being rendered every 10 seconds
        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                // TODO: get new list of nearby Entity objects
                println("DEBUG - INSIDE UPDATE ANCHORS TIMER TASK")
                renderer.updateAnchors(model.getEntities())
            }
        }, 0, 10000)


    }


    private fun configureSession(session: Session) {
        session.configure(
            session.config.apply {
                geospatialMode = Config.GeospatialMode.ENABLED
            }
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (!GeoPermissionsHelper.hasGeoPermissions(this)) {
            // Use toast instead of snackbar here since the activity will exit.
            Toast.makeText(this, "Camera and location permissions are needed to run this application", Toast.LENGTH_LONG)
                .show()
            if (!GeoPermissionsHelper.shouldShowRequestPermissionRationale(this)) {
                // Permission denied with checking "Do not ask again".
                GeoPermissionsHelper.launchPermissionSettings(this)
            }
            finish()
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        FullScreenHelper.setFullScreenOnWindowFocusChanged(this, hasFocus)
    }
}