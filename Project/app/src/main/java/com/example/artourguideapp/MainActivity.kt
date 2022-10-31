package com.example.artourguideapp

import android.content.Intent
import android.graphics.PointF
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.artourguideapp.entities.*
import org.json.JSONArray
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private val server: String = ""
    private val model = Model()
    private val view = UserView()
    private val user = null
    private val controller = Controller(server, model, view, user)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_building_info)

        DummyBuildingEntities.initialize()
        updateStructures()

        val intent = Intent(this, GeospatialActivity::class.java)
        startActivity(intent)
    }

    // update structures json file if needed, and create java objects of each json entry if remote json is different
    private fun updateStructures() {
        thread {
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

                // get remote db structures json
                val connection = URL("https://us-central1-ar-tour-guide-admin-panel.cloudfunctions.net/app/db")
                    .openConnection() as HttpURLConnection
                // set remote json string
                remoteStructuresJsonStr = JSONArray(
                    connection.inputStream.bufferedReader().readText()
                ).toString(4)

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
                val buildings: MutableList<BuildingEntity> = mutableListOf()
                val landmarks: MutableList<LandmarkEntity> = mutableListOf()
                val entities: MutableList<Entity> = mutableListOf()

                val structuresJsonArr = JSONArray(remoteStructuresJsonStr)
                for (i in 0 until structuresJsonArr.length()) {
                    val currentStructure = structuresJsonArr.getJSONObject(i)
                    val currentScrapedData = currentStructure.getJSONObject("scrapedData")
                    val buildingName = currentScrapedData.getString("buildingName")
                    val entityData: EntityData
                    // if landmark, add to landmark list
                    if (currentStructure.getBoolean("isLandmark")) {
                        // TODO: ALTER SCRAPER TO HAVE CORRECT AND REMAINING FIELDS
                        entityData =
                            LandmarkData(buildingName, "", "", "",
                        )
                    }
                    // otherwise it's a building so add to building list
                    else {
                            // TODO: ALTER SCRAPER TO HAVE REMAINING FIELDS
                        entityData =
                            BuildingData(
                                buildingName,
                                "", currentScrapedData.getJSONArray("buildingTypes").toString(),
                                currentScrapedData.getJSONArray("departmentsOffices").toString(),
                                "", "",
                                currentScrapedData.getJSONArray("computerLabs").toString(),
                                "", "", ""
                        )

                    }

                    /* in addition, create abstract entity objects for each structure */

                    // list of coordinates as proper objects
                    val coordinates: MutableList<PointF> = currentStructure.getString("coordinates")
                        .split("(?<=\\))(,\\s*)(?=\\()".toRegex()).map { it.substring(1, it.length - 1) }
                        .map { PointF(it.split(",")[0].toFloat(), it.split(",")[1].toFloat()) }
                        .toMutableList()

                    // set name and location info
                    val location = Location(buildingName)

                    // TODO: REPLACE THIS PLACEHOLDER WITH ACTUAL CENTRAL COORDINATES
                    location.latitude = coordinates[0].x.toDouble()
                    location.longitude = coordinates[0].y.toDouble()

                    // Add building entity if building
                    if (entityData is BuildingData) {
                        buildings.add(
                            // TODO: ALTER SCRAPER TO HAVE REMAINING FIELDS
                            BuildingEntity(buildingName, mutableListOf(), location, entityData)
                        )
                    }
                    // add landmark entity if landmark
                    else if (entityData is LandmarkData) {
                        landmarks.add(
                            // TODO: ALTER SCRAPER TO HAVE REMAINING FIELDS
                            LandmarkEntity(buildingName, mutableListOf(), location, entityData)
                        )
                    }

                }

                // Add all buildings and landmarks to entity list
                entities.addAll(buildings)
                entities.addAll(landmarks)

                // Add all Dummy Entities to list
                // TODO: Remove later
                entities.addAll(DummyBuildingEntities.entityList)

                // pass off lists to controller to give to model
                controller.addBuildings(buildings)
                controller.addLandmarks(landmarks)
                controller.addEntities(entities)

            } catch (ioException: IOException) {
                ioException.printStackTrace()
            }

            // print all entities from model
            for (entity in model.getEntities()) {
                Log.d("entity", entity.getEntityData().toString())
            }
        }
    }
}