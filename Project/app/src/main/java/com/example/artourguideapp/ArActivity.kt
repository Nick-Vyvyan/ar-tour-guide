package com.example.artourguideapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.artourguideapp.entities.Entity
import com.example.artourguideapp.navigation.Navigation
import com.example.artourguideapp.navigation.Tour
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.ar.core.exceptions.CameraNotAvailableException
import com.google.ar.sceneform.ArSceneView
import java.util.*

/**
 * The main activity of the app. Uses Sceneform and Google's ARCore to view the world and
 * render AR elements. Contains functionality for searching, touring, navigating, and looking
 * at a campus map.
 */
class ArActivity : AppCompatActivity() {

    //region Private Variables

    /** Main controller for accessing entities */
    private val controller = Controller()

    /** AR Scene View from Sceneform */
    private lateinit var arSceneView: ArSceneView

    //endregion

    //region Activity Functions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ar)

        // Get ARSceneView and navigation button
        arSceneView = findViewById(R.id.arSceneView)

        // Initialize Navigation
        Navigation.init(arSceneView, this)

        // Initialize Tour feature
        Tour.init(this)

        // initialize all entity AR nodes
        initializeEntityNodes()

        // Schedule anchor placement
        scheduleInitialAnchorPlacements()

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

    /** Initialize entity nodes. Must be called in order to view EntityNode's in AR */
    private fun initializeEntityNodes() {
        for (entity: Entity in controller.getEntities()) {
            entity.initNode(this)
        }
    }

    /** Create search button */
    private fun createSearchButton() {
        // search button on-click listener
        val fab = findViewById<FloatingActionButton>(R.id.search_fab)
        fab.setOnClickListener {
            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
        }
    }

    /** Create map button */
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

    /** Schedule the initial anchor placement update (very frequent) */
    private fun scheduleInitialAnchorPlacements() {
        val delay: Long = 0 // waits this many ms before attempting
        val interval = AppSettings.INITIAL_ANCHOR_SET_INTERVAL_MS // updates after this many ms continuously

        Timer().schedule(object: TimerTask() {
            override fun run() {
                runOnUiThread {
                    if (!AnchorHelper.initialAnchorsPlaced) {
                        Log.d("ANCHOR HELPER", "Attempting to set initial anchors...")
                        AnchorHelper.setAnchors(arSceneView, controller.getEntities())
                    }
                    else {
                        scheduleSecondaryAnchorPlacements()
                        cancel()
                    }
                }
            }
        },delay, interval)
    }

    /** Schedule the secondary anchor placement update (less frequent) */
    private fun scheduleSecondaryAnchorPlacements() {
        val delay: Long = 0 // waits this many ms before attempting
        val interval = AppSettings.ANCHOR_SET_INTERVAL_MS // updates after this many ms continuously

        Timer().schedule(object: TimerTask() {
            override fun run() {
                runOnUiThread {
                    Log.d("ANCHOR HELPER", "Secondary anchor placements")
                    AnchorHelper.setAnchors(arSceneView, controller.getEntities())
                }
            }
        },delay, interval)
    }

    //endregion
}