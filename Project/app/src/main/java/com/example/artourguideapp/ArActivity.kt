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
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.rendering.ViewRenderable
import java.util.*


class ArActivity : AppCompatActivity() {

    lateinit var arSceneView: ArSceneView
    lateinit var arButtonRenderable: ViewRenderable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ar)

        arSceneView = findViewById(R.id.arSceneView)

        ViewRenderable.builder().setView(this, R.layout.ar_button).build()
            .thenAccept {renderable -> arButtonRenderable = renderable}

        ActivityCompat.requestPermissions(
            this, arrayOf(CAMERA, ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION), 0)


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
            return;
        }

        if (arSceneView.getSession() == null) {
            // If the session wasn't created yet, don't resume rendering.
            // This can happen if ARCore needs to be updated or permissions are not granted yet.
            try {
                var lightEstimationMode = Config.LightEstimationMode.ENVIRONMENTAL_HDR;
                var session: Session = Session(this);
                // IMPORTANT!!!  ArSceneView requires the `LATEST_CAMERA_IMAGE` non-blocking update mode.
                var config = Config(session);
                config.updateMode = Config.UpdateMode.LATEST_CAMERA_IMAGE;
                config.lightEstimationMode = lightEstimationMode;
                config.geospatialMode = Config.GeospatialMode.ENABLED
                session.configure(config);

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

            // Make an ArNode and set its anchor to the testAnchor
            var arAnchorNode = AnchorNode(testAnchor)
            arAnchorNode.parent = arSceneView.scene

            var arButtonNode = Node()
            arButtonNode.renderable = arButtonRenderable

            var arButton: Button = arButtonRenderable.view as Button
            arButton.setOnClickListener {
                DummyBuildingEntities.commFacilityEntity.getDialogFragment().show(supportFragmentManager, "CF")
            }

            arAnchorNode.addChild(arButtonNode)
        }
    }
}