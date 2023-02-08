package com.example.artourguideapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PointF
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.ViewManager
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.example.artourguideapp.entities.*
import kotlin.concurrent.thread
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class StartupActivity : AppCompatActivity() {

    //region Private Variables

    private lateinit var loadingText: TextView
    private lateinit var progressText: TextView
    private lateinit var appSettingsButton: Button

    //endregion

    //region Activity Functions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup)

        initializeUI()
    }

    override fun onResume() {
        super.onResume()

        if (!hasPermissions())
            requestPermissions()
        else {
            initializeEntitiesAndStart()
        }
    }

    //endregion

    //region Permissions

    private fun requestPermissions() {
        val permissionsToRequest = mutableListOf<String>()

        // Check for camera and location permissions and add to list if not yet permitted

        if (!hasCameraPermission()) {
            permissionsToRequest.add(Manifest.permission.CAMERA)
        }

        if (!hasFineLocationPermission()) {
            permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        if (!hasCoarseLocationPermission()) {
            permissionsToRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        // Request any needed permissions

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), AppSettings.PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            AppSettings.PERMISSION_REQUEST_CODE -> {
                if (hasPermissions()) {
                    loadingText.text = "Loading…"
                    loadStructuresFromJsonAndStart()
                    //loadDummyEntitiesAndStart()
                }
                else {
                    loadingText.text = "Waiting on permissions…"
                    progressText.text = "Please open app settings"
                }
            }
        }
    }

    private fun hasPermissions() = hasCameraPermission() && hasCoarseLocationPermission() && hasFineLocationPermission()

    private fun hasCameraPermission() =
        ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

    private fun hasFineLocationPermission() =
        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

    private fun hasCoarseLocationPermission() =
        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

    //endregion

    //region Startup Functions

    // Initialize all UI elements
    private fun initializeUI() {
        loadingText = findViewById(R.id.loadingText)
        loadingText.text = "Requesting Camera and Location permission…"

        progressText = findViewById(R.id.progressText)
        progressText.text = ""

        appSettingsButton = findViewById(R.id.appSettingsButton)
        appSettingsButton.text = "App Settings"
        appSettingsButton.setOnClickListener() {
            //Open Permissions
            var intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            var uri = Uri.fromParts("package", packageName, null)
            intent.setData(uri)
            startActivity(intent)
        }
    }

    // Initialize entities and start AR
    private fun initializeEntitiesAndStart() {
        appSettingsButton.visibility = View.GONE
        loadingText.text = "Loading…"

        if (AppSettings.LOAD_DUMMY_ENTITIES) {
            loadDummyEntitiesAndStart()
        }
        else {
            loadStructuresFromJsonAndStart()
        }
    }

    // Initialize with DummyEntities and start AR
    private fun loadDummyEntitiesAndStart() {
        DummyEntities.initialize(this)
        controller.setEntities(DummyEntities.entityList)
        startArActivity()
    }

    // Initialize with JSON Entities and start AR
    private fun loadStructuresFromJsonAndStart() {
        thread {
            var controller = Controller()
            val searchFilename = "search.json"
            val structuresFilename = "structures.json"

            try {
                var localStructuresJson = getLocalJson(structuresFilename)
                var remoteStructuresJson = getRemoteJson("app/db/")
                updateLocalJson(localStructuresJson, remoteStructuresJson, structuresFilename)

                var localSearchJson = getLocalJson(searchFilename)
                var remoteSearchJson = getRemoteJson("app/search")
                updateLocalJson(localSearchJson, remoteSearchJson, searchFilename)

                // construct search index from json
                var searchJsonMap = JSONObject(remoteSearchJson)
                searchJsonMap = searchJsonMap["index"] as JSONObject
                var searchIndex: MutableMap<String, Array<Int>> = mutableMapOf()

                for (key in searchJsonMap.keys()) {
                    searchIndex[key] = Array((searchJsonMap[key] as JSONArray).length()) { 0 }
                    for (i in 0 until (searchJsonMap[key] as JSONArray).length()) {
                        searchIndex[key]?.set(i, (searchJsonMap[key] as JSONArray).optInt(i))
                    }

                }


                // create structure objects for each object in JSONArray to be used by the app
                val structures: MutableList<Entity> = mutableListOf()

                val structuresJsonArr = JSONArray(remoteStructuresJson)
                for (i in 0 until structuresJsonArr.length()) {
                    val currentStructure = structuresJsonArr.getJSONObject(i)
                    val currentScrapedData = currentStructure.getJSONObject("scrapedData")
                    val websiteLink = currentStructure.getString("websiteLink")
                    val audioFileName = currentScrapedData.getString("audioFileName")
                    val searchId = currentStructure.getInt("id")

                    // if has audio file, download from s3
                    if (audioFileName != "") {
                        try {
                            val connection =
                                URL("https://artourguide.s3.us-west-2.amazonaws.com/$audioFileName")
                                    .openConnection() as HttpURLConnection

                            // set remote json string
                            val audioData = connection.inputStream.readBytes()

                            val outputStreamWriter =
                                DataOutputStream(openFileOutput(audioFileName, MODE_PRIVATE))
                            outputStreamWriter.write(audioData)
                            outputStreamWriter.close()
                        } catch (ioException: IOException) {
                            ioException.printStackTrace()
                        }
                    }

                    // create center point object
                    var centerPointStr = currentStructure.getString("centerPoint")
                    centerPointStr = centerPointStr.substring(1, centerPointStr.length - 1)
                    val centerPointArr = centerPointStr.split(",")
                    val centerPoint =
                        PointF(centerPointArr[0].toFloat(), centerPointArr[1].toFloat())

                    // set name and location info
                    val structureName = currentScrapedData.getString("structureName")
                    val location = Location(structureName)
                    location.latitude = centerPoint.x.toDouble()
                    location.longitude = centerPoint.y.toDouble()

                    runOnUiThread {
                        "Getting structure $i of ${structuresJsonArr.length()}…".also { progressText.text = it }
                        
                        // if landmark, add as LandmarkEntity
                        if (currentStructure.getBoolean("isLandmark")) {
                            structures.add(
                                LandmarkEntity(
                                    location,
                                    LandmarkData(
                                        structureName,
                                        currentScrapedData.getString("description"),
                                        audioFileName, websiteLink
                                    ),
                                    searchId
                                )
                            )
                        }
                        // otherwise it's a building, so add as BuildingEntity
                        else {
                            structures.add(
                                BuildingEntity(
                                    location,
                                    BuildingData(
                                        structureName,
                                        currentScrapedData.getString("buildingCode"),
                                        currentScrapedData.getString("structureTypes"),
                                        currentScrapedData.getString("departmentsOffices"),
                                        currentScrapedData.getString("accessibilityInfo"),
                                        currentScrapedData.getString("genderNeutralRestrooms"),
                                        currentScrapedData.getString("computerLabs"),
                                        currentScrapedData.getString("dining"),
                                        audioFileName, websiteLink
                                    ),
                                    searchId
                                )
                            )
                        }
                    }
                }





                controller.setSearchIndex(searchIndex)
                // add structure entities to app
                controller.setEntities(structures)

                runOnUiThread {
                    progressText.text = "Starting AR session…"
                    startArActivity()
                }
            } catch (ioException: IOException) {
                ioException.printStackTrace()
            }
        }
    }


    // Start AR and finish StartupActivity
    private fun startArActivity() {
        var arActivityIntent = Intent(this, ArActivity::class.java)
        startActivity(arActivityIntent)
        finish()
    }

    private fun getLocalJson(filename: String): String {
        try {
            // read in current structures json if one exists
            val inputStream: InputStream = openFileInput(filename)

            val inputStreamReader = InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            var receiveString: String?
            val stringBuilder = StringBuilder()

            // build json string one line at a time
            while (bufferedReader.readLine().also { receiveString = it } != null) {
                stringBuilder.append(receiveString)
            }
            inputStream.close()

            // set local json string
            return stringBuilder.toString()

        } catch (fnfException: FileNotFoundException) {
            // otherwise, create empty structures json file
            try {
                val outputStreamWriter =
                    OutputStreamWriter(openFileOutput(filename, MODE_PRIVATE))
                outputStreamWriter.write("")
                outputStreamWriter.close()
            } catch (ioException: IOException) {
                ioException.printStackTrace()
            }
            return ""
        }
    }

    private fun getRemoteJson(path: String): String {
        while (true) {
            try {
                // get remote db json
                val connection =
                    URL("https://us-central1-ar-tour-guide-admin-panel.cloudfunctions.net/$path")
                        .openConnection() as HttpURLConnection
                Log.d("test", "made it to open connection")
                // set remote json string
                return connection.inputStream.bufferedReader().readText()
            } catch (fnfException: FileNotFoundException) {
                ""
            }
        }
    }

    private fun updateLocalJson(localJson: String, remoteJson: String, filename: String) {
        // update local file with remote db json if different
        if (localJson != remoteJson) {
            try {
                val outputStreamWriter =
                    OutputStreamWriter(openFileOutput(filename, MODE_PRIVATE))
                outputStreamWriter.write(remoteJson)
                outputStreamWriter.close()
            } catch (ioException: IOException) {
                ioException.printStackTrace()
            }
        }
    }
}