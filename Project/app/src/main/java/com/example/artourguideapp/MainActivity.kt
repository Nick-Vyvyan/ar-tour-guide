package com.example.artourguideapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import org.json.JSONArray
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        updateStructures()

        val intent = Intent(this, GpsActivity::class.java)
        startActivity(intent)
    }

    // update structures json file if needed, and create java objects of each json entry if remote json is different
    private fun updateStructures() {
        thread {
            var localStructuresJsonStr = ""
            var remoteStructuresJsonStr = ""
            val gson = Gson()

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

                        // TODO: Create structure objects for each object in JSONArray to be used by the app.
                    } catch (ioException: IOException) {
                        ioException.printStackTrace()
                    }
                }
            } catch (ioException: IOException) {
                ioException.printStackTrace()
            }
        }
    }
}