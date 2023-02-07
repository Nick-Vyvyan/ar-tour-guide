package com.example.artourguideapp.navigation

import android.app.Activity
import com.example.artourguideapp.R
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable

class NavigationWaypointNode(var activity: Activity): Node() {

    private val WAYPOINT_SCALE = Vector3(5f, 5f, 5f)
    private val WAYPOINT_ROTATION = Quaternion.lookRotation(Vector3.down(), Vector3.up())
    private val VERTICAL_DISPLACEMENT = 5f

    init {
        // Build arrow
        ModelRenderable.builder()
            .setSource(activity, R.raw.wwu_arrow)
            .setIsFilamentGltf(true)
            .build()
            .thenAccept { newRenderable ->
                renderable = newRenderable
            }

        // Set scale
        worldScale = WAYPOINT_SCALE
        worldRotation = WAYPOINT_ROTATION
        localPosition.y = VERTICAL_DISPLACEMENT
    }

    override fun onActivate() {
        super.onActivate()

        if (scene == null) {
            throw IllegalStateException("AR Scene is null!")
        }
    }

    override fun onUpdate(frameTime: FrameTime?) {
        super.onUpdate(frameTime)

        if (scene == null) {
            return
        }

        if (localPosition.y != VERTICAL_DISPLACEMENT) {
            localPosition.y = VERTICAL_DISPLACEMENT
        }
    }
}