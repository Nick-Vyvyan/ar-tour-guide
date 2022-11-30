package com.example.artourguideapp

import android.Manifest.permission.*
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.artourguideapp.entities.DummyBuildingEntities
import com.example.artourguideapp.entities.EntityFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.ar.core.Config
import com.google.ar.core.Session
import com.google.ar.core.exceptions.CameraNotAvailableException
import com.google.ar.core.exceptions.UnavailableException
import com.google.ar.sceneform.ArSceneView
import java.util.*

class MainActivity : AppCompatActivity() {
    private val server: String = ""
    private val model = Model()
    private val view = UserView()
    private val user = null
    private val controller = Controller(server, model, view, user)

    // AR SceneForm Variables
    lateinit var arSceneView: ArSceneView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        // update structures for app
//        val updateStructuresIntent = Intent(this, UpdateStructures::class.java)
//        startActivity(updateStructuresIntent)

        // Init Entity Objects
        DummyBuildingEntities.initialize(this)
        controller.addEntities(DummyBuildingEntities.entityList)
        EntityFactory.updateStructures(model, controller, this)

        // get ARSceneView
        arSceneView = findViewById(R.id.arSceneView)

        // Request permissions for AR and Geospatial
        ActivityCompat.requestPermissions(
            this, arrayOf(
                CAMERA,
                ACCESS_FINE_LOCATION,
                ACCESS_COARSE_LOCATION
            ), 0)

        Timer().schedule(object: TimerTask() {
            override fun run() {
                for (entity in model.getEntities()) {
                    runOnUiThread {
                        println("DEBUG - ATTEMPTING TO SET ANCHOR FOR ${entity.getName()}")
                        AnchorHelper.attemptSetAnchor(entity, arSceneView)
                    }
                }
            }
        },2000, 3000)

        // search button on-click listener
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
        }
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

        if (arSceneView.getSession() == null) {
            // If the session wasn't created yet, don't resume rendering.
            // This can happen if ARCore needs to be updated or permissions are not granted yet.
            try {
                var session = Session(this)
                // IMPORTANT!!!  ArSceneView requires the `LATEST_CAMERA_IMAGE` non-blocking update mode.
                var config = Config(session)
                config.updateMode = Config.UpdateMode.LATEST_CAMERA_IMAGE
                config.lightEstimationMode = Config.LightEstimationMode.ENVIRONMENTAL_HDR
                config.geospatialMode = Config.GeospatialMode.ENABLED
                config.planeFindingMode = Config.PlaneFindingMode.DISABLED
                session.configure(config)

                if (session == null) {
                    // cameraPermissionRequested = DemoUtils.hasCameraPermission(this);
                    return;
                } else {
                    arSceneView.session = session;
                    arSceneView.scene.camera.farClipPlane = AnchorHelper.PROXIMITY_DISTANCE.toFloat()
                }
            } catch (e: UnavailableException) {
                //DemoUtils.handleSessionException(this, e);
            }
        }

        try {
            arSceneView.resume();
        } catch (ex: CameraNotAvailableException) {
            // DemoUtils.displayError(this, "Unable to get camera", ex);
            finish();
            return;
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