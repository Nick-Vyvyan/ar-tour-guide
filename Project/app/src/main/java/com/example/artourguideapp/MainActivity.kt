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

        DummyBuildingEntities.initialize()
        EntityFactory.updateStructures(model, controller, this)

        val intent = Intent(this, ArActivityTest::class.java)
        startActivity(intent)
    }

}