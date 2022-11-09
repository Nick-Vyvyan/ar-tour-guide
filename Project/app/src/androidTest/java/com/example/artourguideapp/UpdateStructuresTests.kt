package com.example.artourguideapp

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.json.JSONArray
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

@RunWith(AndroidJUnit4::class)
class UpdateStructuresTests {
    private lateinit var appContext : Context
    @Before
    fun createAppContext() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun getStructuresFile() {
        // attempt to read in current structures json
        appContext.openFileInput("structures.json")
    }

    @Test
    fun isStructuresFileParsable() {
        // attempt to read in current structures json
        val inputStream: InputStream = appContext.openFileInput("structures.json")
        val inputStreamReader = InputStreamReader(inputStream)
        val bufferedReader = BufferedReader(inputStreamReader)
        var receiveString: String?
        val stringBuilder = StringBuilder()

        // build json string one line at a time
        while (bufferedReader.readLine().also { receiveString = it } != null) {
            stringBuilder.append(receiveString)
        }
        inputStream.close()

        // attempt to parse json as json array
        JSONArray(stringBuilder.toString())
    }
}
