package com.example.artourguideapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.artourguideapp.entities.Entity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.ar.core.exceptions.CameraNotAvailableException
import com.google.ar.sceneform.ArSceneView
import java.util.*


class ArActivity : AppCompatActivity() {

    // Main controller for accessing entities
    private val controller = Controller()

    // AR SceneForm Variables
    lateinit var arSceneView: ArSceneView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ar)

        // get ARSceneView
        arSceneView = findViewById(R.id.arSceneView)

        // initialize all entity AR nodes
        initializeEntityNodes()

        // Schedule AR element updating
        scheduleAnchorPlacements()
        scheduleNodeUpdates()

        // Create map button
        createMapButton()

        // Create search button
        createSearchButton()
    }

    private fun createMapButton() {
        // map button on-click listener
        val fab = findViewById<FloatingActionButton>(R.id.map_fab)
        fab.setOnClickListener {
            val mapIntent = Intent(this, MapActivity::class.java)
            startActivity(mapIntent)
        }
    }

    private fun createSearchButton() {
        // search button on-click listener
        val fab = findViewById<FloatingActionButton>(R.id.search_fab)
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
        val interval = AnchorHelper.UPDATE_NODE_INTERVAL_MS // updates after this many ms continuously

        Timer().schedule(object: TimerTask() {
            override fun run() {
                runOnUiThread {
                    AnchorHelper.scheduledUpdateNodes(arSceneView, controller.getEntities())
                }
            }
        },delay, interval)
    }

    private fun initializeEntityNodes() {
        for (entity: Entity in controller.getEntities()) {
            entity.initNode(this)
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