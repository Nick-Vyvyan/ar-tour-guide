package com.example.artourguideapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    val server: String = ""
    val model = Model()
    val view = View()
    val user = null
    val controller = Controller(server, model, view, user)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_building_info)

        DummyBuildingEntities.initialize()
        updateStructures()

        val intent = Intent(this, GpsActivity::class.java)
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

                    // create structure objects for each object in JSONArray to be used by the app
                    val buildings: MutableList<BuildingData> = mutableListOf()
                    val landmarks: MutableList<SculptureData> = mutableListOf()

                    val structuresJsonArr = JSONArray(remoteStructuresJsonStr)
                    for (i in 0 until structuresJsonArr.length()) {
                        val currentStructure = structuresJsonArr.getJSONObject(i)
                        val currentScrapedData = currentStructure.getJSONObject("scrapedData")
                        // if landmark, add to landmark list
                        if (currentStructure.getBoolean("isLandmark")) {
                            landmarks.add(
                                // TODO: ALTER SCRAPER TO HAVE CORRECT AND REMAINING FIELDS
                                SculptureData(currentScrapedData.getString("buildingName"),
                                "", "", ""
                            ))
                        }
                        // otherwise it's a building so add to building list
                        else {
                            buildings.add(
                                // TODO: ALTER SCRAPER TO HAVE REMAINING FIELDS
                                BuildingData(
                                    currentScrapedData.getString("buildingName"),
                                    "", currentScrapedData.getJSONArray("buildingTypes").toString(),
                                    currentScrapedData.getJSONArray("departmentsOffices").toString(),
                                    "", "",
                                    currentScrapedData.getJSONArray("computerLabs").toString(),
                                    "", ""
                                ))
                        }

                        // pass off lists to controller to give to model
                        controller.addBuildings(buildings)
                        controller.addLandmarks(landmarks)

                        // get lists from model
                        val modelBuildings: MutableList<BuildingData> = controller.getBuildings()
                        val modelLandmarks: MutableList<SculptureData> = controller.getLandmarks()

                        // print all buildings from model
                        for (j in 0 until modelBuildings.size) {
                            Log.d("building", modelBuildings[j].toString())
                        }

                        // print all landmarks from model
                        for (j in 0 until modelLandmarks.size) {
                            Log.d("landmark", modelLandmarks[j].toString())
                        }
                    }
                }
            } catch (ioException: IOException) {
                ioException.printStackTrace()
            }
        }
    }
}