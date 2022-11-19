package com.example.artourguideapp

import android.Manifest.permission.*
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.artourguideapp.entities.DummyBuildingEntities
import com.google.ar.core.Config
import com.google.ar.core.Session
import com.google.ar.core.TrackingState
import com.google.ar.core.exceptions.CameraNotAvailableException
import com.google.ar.core.exceptions.UnavailableException
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.ArSceneView
import com.google.ar.sceneform.rendering.ViewRenderable
import java.util.*


class ArActivityTest : AppCompatActivity() {

    lateinit var arSceneView: ArSceneView
    lateinit var arButtonRenderable: ViewRenderable
    lateinit var arAnchorNode: AnchorNode
    lateinit var arButtonTest: Button
    lateinit var arButtonRenderableTest: ViewRenderable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ar)

        // Get the ArSceneView from UML
        arSceneView = findViewById(R.id.arSceneView)


        // Build the arButtonRenderable from its UML
        ViewRenderable.builder().setView(this, R.layout.ar_button).build()
            .thenAccept {renderable -> arButtonRenderable = renderable}

        // Programmatically build an ar button without a UML
        arButtonTest = Button(this)
        arButtonTest.text = "WKRC"
        arButtonTest.setOnClickListener {
            DummyBuildingEntities.wadeKingEntity.getDialogFragment().show(supportFragmentManager, "CF")
        }

        ViewRenderable.builder().setView(this, arButtonTest).build()
            .thenAccept {renderable -> arButtonRenderableTest = renderable}

        // Request permissions for AR and Geospatial
        ActivityCompat.requestPermissions(
            this, arrayOf(CAMERA, ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION), 0)


        // Attempt to make a button at current position every 5 seconds after a 1 second delay
        Timer().schedule(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    attemptToSetAnchor()
                }
            }
        }, 1000, 5000)
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
                session.configure(config)

                if (session == null) {
                    // cameraPermissionRequested = DemoUtils.hasCameraPermission(this);
                    return;
                } else {
                    arSceneView.session = session;
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun attemptToSetAnchor() {

        // get Earth
        val earth = arSceneView.session?.earth
        if (earth?.trackingState == TrackingState.TRACKING) {

            // Create anchor at current position
            var testAnchor = earth.createAnchor(
                earth.cameraGeospatialPose.latitude,
                earth.cameraGeospatialPose.longitude,
                earth.cameraGeospatialPose.altitude,
                0f, 0f, 0f, 0f
            )

            // Make an AnchorNode at that anchor and set its renderable field to the arButtonRenderable
            arAnchorNode = AnchorNode(testAnchor)
            arAnchorNode.renderable = arButtonRenderable


            // Set on click listener for the button
            var arButton: Button = arButtonRenderable.view as Button
            arButton.setOnClickListener {
                DummyBuildingEntities.commFacilityEntity.getDialogFragment().show(supportFragmentManager, "CF")
            }
            arButton.text = "CF"

            arAnchorNode.parent = arSceneView.scene

            // Create anchor at current position
            var testAnchor2 = earth.createAnchor(
                earth.cameraGeospatialPose.latitude,
                earth.cameraGeospatialPose.longitude,
                earth.cameraGeospatialPose.altitude - 1,
                0f, 0f, 0f, 0f
            )

            // Make an AnchorNode at that anchor and set its renderable field to the arButtonRenderable
            var arAnchorNode2 = AnchorNode(testAnchor2)
            arAnchorNode2.renderable = arButtonRenderableTest
            arAnchorNode2.parent = arSceneView.scene
        }
    }
}