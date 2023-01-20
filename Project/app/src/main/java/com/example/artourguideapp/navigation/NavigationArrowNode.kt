package com.example.artourguideapp.navigation

import android.app.Activity
import android.widget.TextView
import com.example.artourguideapp.R
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.ViewRenderable
import kotlin.math.round

/** This is an AR Navigation Node that contains an arrow model and distance text.
 *  It points to the current waypoint and displays how much distance remains in the path */
class NavigationArrowNode(var activity: Activity, var currentWaypoint: NavigationWaypointNode) : Node() {

    // Allowed to be modified by Navigation class
    var distanceFromCurrentWaypointToDestinationInMeters = 0f


    // AR Nodes
    private var arrowNode: Node = Node()
    private var textNode: Node = Node()

    // AR TextView
    private lateinit var distanceText: TextView

    // Constants
    private var NODE_LOCAL_POSITION = Vector3(0f, -.6f, -2f)
    private var ARROW_OFFSET = Vector3(0f, .2f, 0f)
    private var TEXT_OFFSET = Vector3(0f, -.2f, 0f)

    init {
        // Build arrow node
        ModelRenderable.builder()
            .setSource(activity, R.raw.wwu_arrow)
            .setIsFilamentGltf(true)
            .build()
            .thenAccept { newRenderable ->
                arrowNode.renderable = newRenderable
                arrowNode.parent = this
                arrowNode.localPosition = Vector3.add(arrowNode.localPosition, ARROW_OFFSET)
            }

        // Build distance text node
        ViewRenderable.builder().setView(activity, R.layout.distance_text).build()
            .thenAccept { renderable ->
                textNode.renderable = renderable
                textNode.parent = this
                textNode.localPosition = Vector3.add(textNode.localPosition, TEXT_OFFSET)
                distanceText = renderable.view as TextView
            }
    }

    override fun onActivate() {
        super.onActivate()

        if (scene == null) {
            throw IllegalStateException("AR Scene is null!")
        }

        localPosition = NODE_LOCAL_POSITION
    }

    override fun onUpdate(frameTime: FrameTime?) {
        super.onUpdate(frameTime)

        if (scene == null || scene?.camera == null || currentWaypoint == null) {
            return
        }

        // Get the vector from the arrow to the destination
        val currentWaypointPositionFlat = Vector3(currentWaypoint!!.worldPosition.x, scene!!.camera.worldPosition.y, currentWaypoint!!.worldPosition.z)

        val arrowDirection = Vector3.subtract(currentWaypointPositionFlat, worldPosition)

        // Updated rotation as Quaternion
        val arrowRotation = Quaternion.lookRotation(arrowDirection, Vector3.up())
        arrowNode.worldRotation = arrowRotation

        // Get the vector from the arrow to the destination
        val textDirection = Vector3.subtract(textNode.worldPosition, scene!!.camera.worldPosition)

        // Updated rotation as Quaternion
        val textRotation = Quaternion.lookRotation(textDirection, Vector3.up())
        textNode.worldRotation = textRotation


        // Get distance to destination
        val distanceToCurrentWaypoint = Vector3.subtract(currentWaypoint!!.worldPosition, worldPosition).length()
        val distanceInMeters = distanceToCurrentWaypoint + distanceFromCurrentWaypointToDestinationInMeters
        val distanceInFeet = round(distanceInMeters * 3.28084).toInt()

        distanceText.text = formattedDistanceText()

    }

    private fun formattedDistanceText(): String {
        // Return variable
        var formattedText: String

        // Get current waypoint distance at same height as user
        val currentWaypointPositionFlat = Vector3(currentWaypoint!!.worldPosition.x, scene!!.camera.worldPosition.y, currentWaypoint!!.worldPosition.z)
        val distanceToCurrentWaypointFlat = Vector3.subtract(currentWaypointPositionFlat, worldPosition).length()

        // Get total distance left and convert to feet
        val distanceInMeters = distanceToCurrentWaypointFlat + distanceFromCurrentWaypointToDestinationInMeters
        val distanceInFeet = distanceInMeters * 3.28084


        // If more than 1000 feet away, convert to miles
        if (distanceInFeet > 1000) {
            val distanceInMiles = round((distanceInFeet / 5280) * 10).toInt() / 10.0
            formattedText = "$distanceInMiles miles away"
        }
        else {
            var formattedDistance: Int
            // Format to hundreds of feet
            if (distanceInFeet > 250) {
                formattedDistance = round(distanceInFeet / 100).toInt() * 100
            }

            // Format to tens of feet
            else if (distanceInFeet > 50)  {
                formattedDistance = round(distanceInFeet / 10).toInt() * 10
            }

            // Format to singles of feet
            else {
                formattedDistance = round(distanceInFeet).toInt()
            }

            formattedText = "$formattedDistance feet away"
        }

        return formattedText
    }
}
