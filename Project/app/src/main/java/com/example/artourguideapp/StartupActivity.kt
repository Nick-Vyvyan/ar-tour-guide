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
import android.view.ViewManager
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.example.artourguideapp.entities.*
import kotlin.concurrent.thread
import org.json.JSONArray
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class StartupActivity : AppCompatActivity() {

    //region Private Variables

    private lateinit var loadingText: TextView
    private lateinit var progressText: TextView
    private lateinit var appSettingsButton: Button
    private val PERMISSION_REQUEST_CODE = 12345

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
        var permissionsToRequest = mutableListOf<String>()

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
            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (hasPermissions()) {
                    loadingText.text = "Loading..."
                    loadStructuresFromJsonAndStart()
                    //loadDummyEntitiesAndStart()
                }
                else {
                    loadingText.text = "Waiting on permissions..."
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
        loadingText.text = "Requesting Camera and Location permission..."

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
        (appSettingsButton.parent as ViewManager).removeView(appSettingsButton)
        loadingText.text = "Loading..."
        loadStructuresFromJsonAndStart()
//        loadDummyEntitiesAndStart()
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
            var localStructuresJsonStr = ""
            var remoteStructuresJsonStr = ""

            try {
                try {
                    // read in current structures json if one exists
                    val inputStream: InputStream = openFileInput("structures.json")

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
                    localStructuresJsonStr = stringBuilder.toString()
                } catch (fnfException: FileNotFoundException) {
                    // otherwise, create empty structures json file
                    try {
                        val outputStreamWriter =
                            OutputStreamWriter(openFileOutput("structures.json", MODE_PRIVATE))
                        outputStreamWriter.write("")
                        localStructuresJsonStr = ""
                        outputStreamWriter.close()
                    } catch (ioException: IOException) {
                        ioException.printStackTrace()
                    }
                }

                var retrievingDB = false
                while (!retrievingDB) {
                    retrievingDB = true
                    try {
                        // get remote db structures json
                        val connection =
                            URL("https://us-central1-ar-tour-guide-admin-panel.cloudfunctions.net/app/db")
                                .openConnection() as HttpURLConnection
                        // set remote json string
                        remoteStructuresJsonStr = JSONArray(
                            connection.inputStream.bufferedReader().readText()
                        ).toString(4)
                    } catch (fnfException: FileNotFoundException) {
                        retrievingDB = false
                    }
                }

                // update structures file with remote db json if different
                if (localStructuresJsonStr != remoteStructuresJsonStr) {
                    try {
                        val outputStreamWriter =
                            OutputStreamWriter(openFileOutput("structures.json", MODE_PRIVATE))
                        outputStreamWriter.write(remoteStructuresJsonStr)
                        outputStreamWriter.close()
                    } catch (ioException: IOException) {
                        ioException.printStackTrace()
                    }
                }

                // create structure objects for each object in JSONArray to be used by the app
                val structures: MutableList<Entity> = mutableListOf()

                val structuresJsonArr = JSONArray(remoteStructuresJsonStr)
                for (i in 0 until structuresJsonArr.length()) {
                    val currentStructure = structuresJsonArr.getJSONObject(i)
                    val currentScrapedData = currentStructure.getJSONObject("scrapedData")
                    val websiteLink = currentStructure.getString("websiteLink")
                    val audioFileName = currentScrapedData.getString("audioFileName")

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
                        progressText.text = "" + i + " of " + structuresJsonArr.length()
                        // if landmark, add as LandmarkEntity
                        if (currentStructure.getBoolean("isLandmark")) {
                            structures.add(
                                LandmarkEntity(
                                    location,
                                    LandmarkData(
                                        structureName,
                                        currentScrapedData.getString("description"),
                                        audioFileName, websiteLink
                                    )
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
                                    )
                                )
                            )
                        }
                    }
                }

                // add structure entities to app
                controller.setEntities(structures)

                runOnUiThread {
                    progressText.text = "Starting AR session"
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

    //endregion
}