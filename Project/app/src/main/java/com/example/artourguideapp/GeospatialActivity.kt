///*
// * Copyright 2022 Google LLC
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
///** This File has been changed since its original distribution*/
//
//package com.example.artourguideapp
//
//import android.graphics.Color
//import android.graphics.PointF
//import android.location.Location
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.os.Handler
//import android.os.Looper
//import android.util.Log
//import android.widget.Toast
//import com.example.artourguideapp.arcore.java.com.google.ar.core.examples.java.common.helpers.FullScreenHelper
//import com.example.artourguideapp.arcore.java.com.google.ar.core.examples.java.common.samplerender.SampleRender
//import com.google.ar.core.Config
//import com.google.ar.core.Session
//import com.google.ar.core.codelabs.hellogeospatial.*
//import com.google.ar.core.codelabs.hellogeospatial.helpers.*
//import com.google.ar.core.exceptions.*
//import com.google.ar.core.exceptions.UnavailableUserDeclinedInstallationException
//import com.example.artourguideapp.entities.*
//import com.google.android.gms.maps.model.LatLng
//import com.google.android.gms.maps.model.Marker
//import com.google.android.gms.maps.model.MarkerOptions
//import kotlinx.coroutines.Runnable
//import org.json.JSONArray
//import java.io.*
//import java.net.HttpURLConnection
//import java.net.URL
//import java.util.Timer
//import java.util.TimerTask
//import kotlin.concurrent.thread
//
//class GeospatialActivity : AppCompatActivity() {
//    companion object {
//        private const val TAG = "GeospatialActivity"
//    }
//
//    lateinit var arCoreSessionHelper: ARCoreSessionLifecycleHelper
//    lateinit var view: HelloGeoView
//    lateinit var renderer: HelloGeoRenderer
//
//
////    var entityList : MutableList<Entity> = mutableListOf()
//    val server: String = ""
//    val model = Model()
//    val userView = UserView()
//    val user = null
//    val controller = Controller(server, model, userView, user)
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
////        setContentView(R.layout.activity_geospatial)
//
//
//        // Setup ARCore session lifecycle helper and configuration.
//        arCoreSessionHelper = ARCoreSessionLifecycleHelper(this)
//        // If Session creation or Session.resume() fails, display a message and log detailed
//        // information.
//        arCoreSessionHelper.exceptionCallback =
//            { exception ->
//                val message =
//                    when (exception) {
//                        is UnavailableUserDeclinedInstallationException ->
//                            "Please install Google Play Services for AR"
//                        is UnavailableApkTooOldException -> "Please update ARCore"
//                        is UnavailableSdkTooOldException -> "Please update this app"
//                        is UnavailableDeviceNotCompatibleException -> "This device does not support AR"
//                        is CameraNotAvailableException -> "Camera not available. Try restarting the app."
//                        else -> "Failed to create AR session: $exception"
//                    }
//                Log.e(GeospatialActivity.TAG, "ARCore threw an exception", exception)
//                view.snackbarHelper.showError(this, message)
//            }
//
//        // Configure session features.
//        arCoreSessionHelper.beforeSessionResume = ::configureSession
//        lifecycle.addObserver(arCoreSessionHelper)
//
//        // Set up the Hello AR renderer.
//        renderer = HelloGeoRenderer(this)
//        lifecycle.addObserver(renderer)
//
//        // Set up Hello AR UI.
//        view = HelloGeoView(this)
//        lifecycle.addObserver(view)
//        setContentView(view.root)
//
//        SampleRender(view.surfaceView, renderer, assets)
////        renderer.setTestAnchors()
//
//
////        DummyBuildingEntities.initialize()
////        DummyBuildingEntities.entityList.forEach {
////            this.entityList.add(it)
////        }
//
//        updateStructures()
//
//        var markerLocationList : MutableList<Location> = mutableListOf()
//
//        // update anchors being rendered every 10 seconds
//        Timer().scheduleAtFixedRate(object : TimerTask() {
//            override fun run() {
//                // TODO: get new list of nearby Entity objects
//                println("DEBUG - INSIDE UPDATE ANCHORS TIMER TASK")
//                renderer.updateAnchors(model.getEntities())
//
//                // Draw markers on map
//                for (entity in model.getEntities()) {
//                    val location = entity.getCentralLocation()
//                    if (!markerLocationList.contains(location)) {
//                        println("DEBUG - CREATING NEW MARKER AT $location")
//                        markerLocationList.add(location)
//                        runOnUiThread {
//                            view.mapView?.googleMap?.addMarker(
//                                MarkerOptions()
//                                    .position(LatLng(location.latitude, location.longitude))
//                                    .draggable(false)
//                                    .flat(true)
//                            )
//                        }
//                    }
//                }
//            }
//        }, 0, 10000)
//
//
//    }
//
//
//    private fun configureSession(session: Session) {
//        session.configure(
//            session.config.apply {
//                geospatialMode = Config.GeospatialMode.ENABLED
//            }
//        )
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (!GeoPermissionsHelper.hasGeoPermissions(this)) {
//            // Use toast instead of snackbar here since the activity will exit.
//            Toast.makeText(this, "Camera and location permissions are needed to run this application", Toast.LENGTH_LONG)
//                .show()
//            if (!GeoPermissionsHelper.shouldShowRequestPermissionRationale(this)) {
//                // Permission denied with checking "Do not ask again".
//                GeoPermissionsHelper.launchPermissionSettings(this)
//            }
//            finish()
//        }
//    }
//
//    override fun onWindowFocusChanged(hasFocus: Boolean) {
//        super.onWindowFocusChanged(hasFocus)
//        FullScreenHelper.setFullScreenOnWindowFocusChanged(this, hasFocus)
//    }
//
//    // TODO: Put this logic into its own class
//    private fun updateStructures() {
//        thread {
//            var localStructuresJsonStr = ""
//            var remoteStructuresJsonStr = ""
//
//            try {
//                try {
//                    // read in current structures json if one exists
//                    val inputStream: InputStream = openFileInput("structures.json")
//
//                    val inputStreamReader = InputStreamReader(inputStream)
//                    val bufferedReader = BufferedReader(inputStreamReader)
//                    var receiveString: String?
//                    val stringBuilder = StringBuilder()
//
//                    // build json string one line at a time
//                    while (bufferedReader.readLine().also { receiveString = it } != null) {
//                        stringBuilder.append(receiveString)
//                    }
//                    inputStream.close()
//
//                    // set local json string
//                    localStructuresJsonStr = stringBuilder.toString()
//                } catch (fnfException: FileNotFoundException) {
//                    // otherwise, create empty structures json file
//                    try {
//                        val outputStreamWriter =
//                            OutputStreamWriter(openFileOutput("structures.json", MODE_PRIVATE))
//                        outputStreamWriter.write("")
//                        localStructuresJsonStr = ""
//                        outputStreamWriter.close()
//                    } catch (ioException: IOException) {
//                        ioException.printStackTrace()
//                    }
//                }
//
//                // get remote db structures json
//                val connection = URL("https://us-central1-ar-tour-guide-admin-panel.cloudfunctions.net/app/db")
//                    .openConnection() as HttpURLConnection
//                // set remote json string
//                remoteStructuresJsonStr = JSONArray(
//                    connection.inputStream.bufferedReader().readText()
//                ).toString(4)
//
//                // update structures file with remote db json if different
//                if (localStructuresJsonStr != remoteStructuresJsonStr) {
//                    try {
//                        val outputStreamWriter =
//                            OutputStreamWriter(openFileOutput("structures.json", MODE_PRIVATE))
//                        outputStreamWriter.write(remoteStructuresJsonStr)
//                        outputStreamWriter.close()
//                    } catch (ioException: IOException) {
//                        ioException.printStackTrace()
//                    }
//                }
//
//                // create structure objects for each object in JSONArray to be used by the app
//                val buildings: MutableList<BuildingEntity> = mutableListOf()
//                val landmarks: MutableList<LandmarkEntity> = mutableListOf()
//                val entities: MutableList<Entity> = mutableListOf()
//
//                val structuresJsonArr = JSONArray(remoteStructuresJsonStr)
//                for (i in 0 until structuresJsonArr.length()) {
//                    val currentStructure = structuresJsonArr.getJSONObject(i)
//                    val currentScrapedData = currentStructure.getJSONObject("scrapedData")
//                    val buildingName = currentScrapedData.getString("buildingName")
//                    val entityData: EntityData
//                    // if landmark, add to landmark list
//                    if (currentStructure.getBoolean("isLandmark")) {
//                        // TODO: ALTER SCRAPER TO HAVE CORRECT AND REMAINING FIELDS
//                        entityData =
//                            LandmarkData(buildingName, "", "", "",
//                            )
//                    }
//                    // otherwise it's a building so add to building list
//                    else {
//                        // TODO: ALTER SCRAPER TO HAVE REMAINING FIELDS
//                        entityData =
//                            BuildingData(
//                                buildingName,
//                                "", currentScrapedData.getJSONArray("buildingTypes").toString(),
//                                currentScrapedData.getJSONArray("departmentsOffices").toString(),
//                                "", "",
//                                currentScrapedData.getJSONArray("computerLabs").toString(),
//                                "", "", ""
//                            )
//
//                    }
//
//                    /* in addition, create abstract entity objects for each structure */
//
//                    // list of coordinates as proper objects
//                    val coordinates: MutableList<PointF> = currentStructure.getString("coordinates")
//                        .split("(?<=\\))(,\\s*)(?=\\()".toRegex()).map { it.substring(1, it.length - 1) }
//                        .map { PointF(it.split(",")[0].toFloat(), it.split(",")[1].toFloat()) }
//                        .toMutableList()
//
//                    // set name and location info
//                    val location = Location(buildingName)
//
//                    // TODO: REPLACE THIS PLACEHOLDER WITH ACTUAL CENTRAL COORDINATES
//                    location.latitude = coordinates[0].x.toDouble()
//                    location.longitude = coordinates[0].y.toDouble()
//
//                    // Add building entity if building
//                    if (entityData is BuildingData) {
//                        buildings.add(
//                            // TODO: ALTER SCRAPER TO HAVE REMAINING FIELDS
//                            BuildingEntity(buildingName, mutableListOf(), location, entityData)
//                        )
//                    }
//                    // add landmark entity if landmark
//                    else if (entityData is LandmarkData) {
//                        landmarks.add(
//                            // TODO: ALTER SCRAPER TO HAVE REMAINING FIELDS
//                            LandmarkEntity(buildingName, mutableListOf(), location, entityData)
//                        )
//                    }
//
//                }
//
//                // Add all buildings and landmarks to entity list
//                entities.addAll(buildings)
//                entities.addAll(landmarks)
//
//                // Add all Dummy Entities to list
//                // TODO: Remove later
////                entities.addAll(DummyBuildingEntities.entityList)
//
//                // pass off lists to controller to give to model
//                controller.addBuildings(buildings)
//                controller.addLandmarks(landmarks)
//                controller.addEntities(entities)
//
//            } catch (ioException: IOException) {
//                ioException.printStackTrace()
//            }
//
//            // print all entities from model
//            for (entity in model.getEntities()) {
//                Log.d("entity", entity.getEntityData().toString())
//            }
//        }
//    }
//
//
//
//
//
//
//
//
//}