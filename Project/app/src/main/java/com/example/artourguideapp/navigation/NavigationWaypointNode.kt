package com.example.artourguideapp.navigation

import android.app.Activity
import com.example.artourguideapp.AppSettings
import com.example.artourguideapp.R
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable

class NavigationWaypointNode(var activity: Activity): Node() {

    init {
        // Build arrow
        ModelRenderable.builder()
            .setSource(activity, R.raw.wwu_waypoint_arrow)
            .setIsFilamentGltf(true)
            .build()
            .thenAccept { newRenderable ->
                renderable = newRenderable
            }

        // Set scale
        worldScale = AppSettings.WAYPOINT_SCALE
        localPosition.y = AppSettings.WAYPOINT_VERTICAL_DISPLACEMENT
    }

    override fun onActivate() {
        super.onActivate()

        if (scene == null) {
            throw IllegalStateException("AR Scene is null!")
        }

        // Place waypoint arrow initially facing user
        val vectorToCurrentWaypoint = Vector3.subtract(scene!!.camera.worldPosition, worldPosition)
        val arrowRotation = Quaternion.lookRotation(vectorToCurrentWaypoint, Vector3.up())

        worldRotation = arrowRotation
    }

    override fun onUpdate(frameTime: FrameTime?) {
        super.onUpdate(frameTime)

        if (scene == null) {
            return
        }

        if (localPosition.y != AppSettings.WAYPOINT_VERTICAL_DISPLACEMENT) {
            localPosition.y = AppSettings.WAYPOINT_VERTICAL_DISPLACEMENT
        }
    }
}