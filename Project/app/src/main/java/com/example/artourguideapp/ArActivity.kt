package com.example.artourguideapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.artourguideapp.entities.Entity
import com.example.artourguideapp.navigation.Navigation
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.ar.core.exceptions.CameraNotAvailableException
import com.google.ar.sceneform.ArSceneView
import java.util.*


class ArActivity : AppCompatActivity() {

    //region Private Variables

    // Main controller for accessing entities
    private val controller = Controller()

    private lateinit var navButton: Button

    // AR SceneForm Variables
    private lateinit var arSceneView: ArSceneView

    //endregion

    //region Activity Functions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ar)

        // Get ARSceneView and navigation button
        arSceneView = findViewById(R.id.arSceneView)
        navButton = findViewById(R.id.navButton)

        // Initialize Navigation
        Navigation.init(arSceneView, this, navButton)

        // Set AR navigation button properties
        setNavButtonProperties()

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

    //endregion

    //region UI

    // Set navigation button properties
    private fun setNavButtonProperties() {
        navButton.text = "Stop Navigation"
        navButton.setOnClickListener {
            Navigation.stopNavigation()
        }
    }

    // Initialize all Entity nodes. Mu
    private fun initializeEntityNodes() {
        for (entity: Entity in controller.getEntities()) {
            entity.initNode(this)
        }

    }

    // Create the search button
    private fun createSearchButton() {
        // search button on-click listener
        val fab = findViewById<FloatingActionButton>(R.id.search_fab)
        fab.setOnClickListener {
            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
        }
    }

    private fun createMapButton() {
        // map button on-click listener
        val fab = findViewById<FloatingActionButton>(R.id.map_fab)
        fab.setOnClickListener {
            val mapIntent = Intent(this, MapActivity::class.java)
            startActivity(mapIntent)
        }
    }

    //endregion

    //region Scheduling

    // Schedule the anchor placement update
    private fun scheduleAnchorPlacements() {
        val delay: Long = 2000 // waits this many ms before attempting
        val interval = AnchorHelper.ANCHOR_SET_INTERVAL_MS // updates after this many ms continuously

        Timer().schedule(object: TimerTask() {
            override fun run() {
                runOnUiThread {
                    AnchorHelper.setAnchors(arSceneView, controller.getEntities())
                }
            }
        },delay, interval)
    }

    // Schedule updates for all attached Entity nodes
    private fun scheduleNodeUpdates() {
        val delay: Long = 2000 // waits this many ms before attempting
        val interval = AnchorHelper.UPDATE_NODE_INTERVAL_MS // updates after this many ms continuously

        Timer().schedule(object: TimerTask() {
            override fun run() {
                runOnUiThread {
                    AnchorHelper.updateNodes(arSceneView, controller.getEntities())
                }
            }
        },delay, interval)
    }

    //endregion
}