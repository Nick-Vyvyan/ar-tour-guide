package com.example.artourguideapp

import android.Manifest.permission.*
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.artourguideapp.entities.EntityFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.ar.core.exceptions.CameraNotAvailableException
import com.google.ar.sceneform.ArSceneView
import java.util.*

class MainActivity : AppCompatActivity() {
    private val controller = Controller()

    // AR SceneForm Variables
    lateinit var arSceneView: ArSceneView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Init Entity Objects
//        DummyBuildingEntities.initialize(this)
//        controller.addEntities(DummyBuildingEntities.entityList)
        EntityFactory.updateStructures(controller, this)

        // get ARSceneView
        arSceneView = findViewById(R.id.arSceneView)


        requestAppPermissions();
        scheduleAnchorUpdates()
        createSearchButton()
    }

    private fun createSearchButton() {
        // search button on-click listener
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
        }
    }

    private fun scheduleAnchorUpdates() {
        Timer().schedule(object: TimerTask() {
            override fun run() {
                for (entity in controller.getEntities()) {
                    runOnUiThread {
                        println("DEBUG - ATTEMPTING TO SET ANCHOR FOR ${entity.getName()}")
                        AnchorHelper.attemptSetAnchor(entity, arSceneView)
                    }
                }
            }
        },2000, 3000)
    }

    private fun requestAppPermissions() {
        // Request permissions for AR and Geospatial
        ActivityCompat.requestPermissions(
            this, arrayOf(
                CAMERA,
                ACCESS_FINE_LOCATION,
                ACCESS_COARSE_LOCATION
            ), 0)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            // TODO: handle permission denial
//            0 -> {
//                if (!(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                    ActivityCompat.requestPermissions(
//                        this, arrayOf(
//                            CAMERA,
//                            ACCESS_FINE_LOCATION,
//                            ACCESS_COARSE_LOCATION
//                        ), 0)
//                }
//            }
        }
    }

    override fun onResume() {
        super.onResume()

        if (arSceneView == null) {
            return
        }

        if (arSceneView.session == null) {
            ArSessionFactory.createArSession(this, arSceneView)
        }

        try {
            arSceneView.resume()
        } catch (ex: CameraNotAvailableException) {
            // DemoUtils.displayError(this, "Unable to get camera", ex);
            finish()
            return
        }

    }

    override fun onPause() {
        super.onPause()
        if (arSceneView != null) {
            arSceneView.pause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (arSceneView != null) {
            arSceneView.destroy()
        }
    }



}