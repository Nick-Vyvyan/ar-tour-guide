package com.example.artourguideapp.entities

import android.graphics.PointF
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import com.example.artourguideapp.Controller
import com.example.artourguideapp.Model
import com.example.artourguideapp.UpdateStructures
import org.json.JSONArray
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class EntityFactory {
    companion object {

        // update structures json file if needed, and create java objects of each json entry if remote json is different
        fun updateStructures(model: Model, controller: Controller, activity: AppCompatActivity) {

                thread {
                    var localStructuresJsonStr = ""
                    var remoteStructuresJsonStr = ""

                    try {
                        try {
                            // read in current structures json if one exists
                            val inputStream: InputStream = activity.openFileInput("structures.json")

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
                                    OutputStreamWriter(activity.openFileOutput("structures.json", AppCompatActivity.MODE_PRIVATE))
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
                                    OutputStreamWriter(activity.openFileOutput("structures.json", AppCompatActivity.MODE_PRIVATE))
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
                                    val connection = URL("https://artourguide.s3.us-west-2.amazonaws.com/$audioFileName")
                                        .openConnection() as HttpURLConnection

                                    // set remote json string
                                    val audioData = connection.inputStream.readBytes()

                                    val outputStreamWriter =
                                        DataOutputStream(activity.openFileOutput(audioFileName, AppCompatActivity.MODE_PRIVATE))
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
                            val centerPoint = PointF(centerPointArr[0].toFloat(), centerPointArr[1].toFloat())

                            // set name and location info
                            val structureName = currentScrapedData.getString("structureName")
                            val location = Location(structureName)
                            location.latitude = centerPoint.x.toDouble()
                            location.longitude = centerPoint.y.toDouble()

                            // if landmark, add as LandmarkEntity
                            if (currentStructure.getBoolean("isLandmark")) {
                                activity.runOnUiThread {
                                    structures.add(
                                        LandmarkEntity(
                                            structureName, centerPoint, location,
                                            LandmarkData(
                                                structureName,
                                                currentScrapedData.getJSONArray("description").toString(),
                                                audioFileName, websiteLink
                                            )
                                        )
                                    )
                                }
                            }
                            // otherwise it's a building, so add as BuildingEntity
                            else {
                                activity.runOnUiThread {
                                    structures.add(
                                        BuildingEntity(
                                            structureName, centerPoint, location,
                                            BuildingData(
                                                structureName,
                                                currentScrapedData.getJSONArray("structureTypes")
                                                    .toString(),
                                                currentScrapedData.getJSONArray("departmentsOffices")
                                                    .toString(),
                                                currentScrapedData.getJSONArray("accessibilityInfo")
                                                    .toString(),
                                                currentScrapedData.getJSONArray("genderNeutralRestrooms")
                                                    .toString(),
                                                currentScrapedData.getJSONArray("computerLabs").toString(),
                                                currentScrapedData.getJSONArray("dining").toString(),
                                                audioFileName, websiteLink
                                            )
                                        )
                                    )
                                }
                            }

                            // add structure entities to app
                            UpdateStructures.controller.addEntities(structures)

    //                    /* DEBUGGING CODE */
    //
    //                    // get entities list from model
    //                    val modelEntities: MutableList<Entity> = controller.getEntities()
    //
    //                    // print all structure entities from model
    //                    for (j in 0 until modelEntities.size) {
    //                        Log.d("structure", modelEntities[j].toString())
    //                    }
                            for (entity: Entity in model.getEntities()) {
                                entity.initNode(activity)
                            }

                        }
                    } catch (ioException: IOException) {
                        ioException.printStackTrace()
                    }
                }


        }

    }
}