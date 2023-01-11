package com.example.artourguideapp

import android.Manifest.permission.*
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.artourguideapp.entities.DummyEntities
import com.example.artourguideapp.entities.EntityFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.ar.core.exceptions.CameraNotAvailableException
import com.google.ar.sceneform.ArSceneView
import java.util.*

class MainActivity : AppCompatActivity() {

    // Main controller for accessing entities
    private val controller = Controller()

    // AR SceneForm Variables
    lateinit var arSceneView: ArSceneView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        DummyEntities.initialize(this)
//        controller.setEntities(DummyEntities.entityList)

        // Init Entity Objects
        EntityFactory.updateStructures(controller, this)

        // get ARSceneView
        arSceneView = findViewById(R.id.arSceneView)


        requestAppPermissions()
        scheduleAnchorPlacements()
        scheduleNodeUpdates()
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


    private fun scheduleAnchorPlacements() {
        val delay: Long = 2000 // waits this many ms before attempting
        val interval = AnchorHelper.ANCHOR_SET_INTERVAL_MS // updates after this many ms continuously

        Timer().schedule(object: TimerTask() {
            override fun run() {
                runOnUiThread {
                    AnchorHelper.scheduledSetAnchors(arSceneView, controller.getEntities())
                }
            }
        },delay, interval)
    }

    private fun scheduleNodeUpdates() {
        val delay: Long = 2000 // waits this many ms before attempting
        val period = AnchorHelper.UPDATE_NODE_INTERVAL_MS // updates after this many ms continuously

        Timer().schedule(object: TimerTask() {
            override fun run() {
                runOnUiThread {
                    AnchorHelper.scheduledUpdateNodes(arSceneView, controller.getEntities())
                }
            }
        },delay, period)
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
            // TODO: Handle CameraNotAvailableException
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