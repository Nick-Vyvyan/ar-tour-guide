package com.example.artourguideapp.navigation

import android.app.Activity
import android.widget.TextView
import com.example.artourguideapp.AppSettings
import com.example.artourguideapp.R
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.ViewRenderable
import kotlin.math.ceil

/**
 * This is an AR [Node] that contains an arrow model and distance text and is used in Navigation.
 * It points to the current waypoint and displays how much distance remains in the path
 *
 * @property currentWaypoint The [Node] that the arrow will point at
 *
 * @constructor Create a new directional navigation arrow
 *
 * @param activity AR activity
 * @param currentWaypoint The [Node] that the arrow will point at
 */
class NavigationArrowNode(activity: Activity, var currentWaypoint: Node) : Node() {

    //region Public variables
    /**
     * Distance from current waypoint to destination
     */
    var distanceFromCurrentWaypointToDestination = 0.0

    /**
     * Distance to current waypoint
     */
    var distanceToCurrentWaypoint = 0f

    /**
     * Point directly to waypoint
     */
    var pointDirectlyToWaypoint = false

    //endregion

    //region UI Elements

    /** Distance to destination text view */
    private var distanceToDestinationTextView: TextView = activity.findViewById(R.id.distanceToDestinationTextView)

    //endregion

    //region AR Elements

    /** Arrow node */
    private var arrowNode: Node = Node()

    /** Text node */
    private var textNode: Node = Node()

    /** Waypoint distance text */
    private lateinit var waypointDistanceText: TextView

    //endregion

    init {
        // Build arrow node
        ModelRenderable.builder()
            .setSource(activity, R.raw.wwu_navigation_arrow)
            .setIsFilamentGltf(true)
            .build()
            .thenAccept { newRenderable ->
                arrowNode.renderable = newRenderable
                arrowNode.parent = this
                arrowNode.localPosition = Vector3.add(arrowNode.localPosition, AppSettings.ARROW_NODE_ARROW_OFFSET)
            }

        // Build distance text node
        ViewRenderable.builder().setView(activity, R.layout.distance_text).build()
            .thenAccept { renderable ->
                textNode.renderable = renderable
                textNode.parent = arrowNode
                textNode.localPosition = Vector3.add(textNode.localPosition, AppSettings.ARROW_NODE_TEXT_OFFSET)
                waypointDistanceText = renderable.view as TextView
            }
    }

    override fun onActivate() {
        super.onActivate()

        if (scene == null) {
            throw IllegalStateException("AR Scene is null!")
        }

        localPosition = AppSettings.ARROW_NODE_LOCAL_POSITION
        localScale = AppSettings.ARROW_NODE_LOCAL_SCALE
    }

    override fun onUpdate(frameTime: FrameTime?) {
        super.onUpdate(frameTime)

        if (scene == null || scene?.camera == null || currentWaypoint == null) {
            return
        }

        // Point arrow to current waypoint
        val currentWaypointPositionFlat = Vector3(currentWaypoint!!.worldPosition.x, scene!!.camera.worldPosition.y, currentWaypoint!!.worldPosition.z)
        val vectorToCurrentWaypointFlat = Vector3.subtract(currentWaypointPositionFlat, worldPosition)

        // If arrow should point directly to waypoint, do that, otherwise, point to waypoint coordinate at same elevation as user
        var arrowRotation: Quaternion = if (pointDirectlyToWaypoint) {
            val vectorToCurrentWaypoint = Vector3.subtract(currentWaypoint!!.worldPosition, worldPosition)
            Quaternion.lookRotation(vectorToCurrentWaypoint, Vector3.up())
        } else {
            Quaternion.lookRotation(vectorToCurrentWaypointFlat, Vector3.up())
        }

        arrowNode.worldRotation = arrowRotation

        // Point text toward user
        val textDirection = Vector3.subtract(textNode.worldPosition, scene!!.camera.worldPosition)
        val textRotation = Quaternion.lookRotation(textDirection, Vector3.up())
        textNode.worldRotation = textRotation

        distanceToCurrentWaypoint = vectorToCurrentWaypointFlat.length()

        // update UI
        updateUI()
    }

    /**
     * Update visual elements
     *
     */
    private fun updateUI() {
        setDistanceToDestinationText()
        setWaypointDistanceText()
    }

    /**
     * Set waypoint distance text. Distance is formatted to tens of feet when greater than 100.
     * Distance is formatted to singles of feet when less than or equal to 100.
     */
    private fun setWaypointDistanceText() {
        // Set text if current waypoint isn't destination
        if (distanceFromCurrentWaypointToDestination > 1) {
            val distanceToWaypointInFeet = distanceToCurrentWaypoint * 3.28084
            var formattedWaypointDistance: Int

            // Format to tens of feet
            if (distanceToWaypointInFeet > 100)  {
                formattedWaypointDistance = ceil(distanceToWaypointInFeet / 10).toInt() * 10
            }

            // Format to singles of feet
            else {
                formattedWaypointDistance = ceil(distanceToWaypointInFeet).toInt()
            }

            waypointDistanceText.text = "\n$formattedWaypointDistance ft. to waypoint"
        }
        else {
            waypointDistanceText.text = ""
        }
    }

    /**
     * Set distance to destination text. Text is formatted based on the following:
     * - If greater than 1000 feet, display in miles
     * - Else if greater than 500 feet, display in hundreds of feet
     * - Else if greater than 50 feet, display in tens of feet
     * - Else, display in singles of feet
     */
    private fun setDistanceToDestinationText() {
        // Return variable
        var formattedText = ""

        // Get current waypoint distance at same height as user
        val currentWaypointPositionFlat = Vector3(currentWaypoint!!.worldPosition.x, worldPosition.y, currentWaypoint!!.worldPosition.z)
        val distanceToCurrentWaypointFlat = Vector3.subtract(currentWaypointPositionFlat, worldPosition).length()

        // Get total distance left and convert to feet
        val distanceInMeters = distanceToCurrentWaypointFlat + distanceFromCurrentWaypointToDestination
        val distanceInFeet = distanceInMeters * 3.28084

        // If more than 1000 feet away, convert to miles
        if (distanceInFeet > 1000) {
            val distanceInMiles = ceil((distanceInFeet / 5280) * 10).toInt() / 10.0
            formattedText += "$distanceInMiles miles away"
        }
        else {
            var formattedDistance: Int

            // Format to hundreds of feet
            if (distanceInFeet > 500) {
                formattedDistance = ceil(distanceInFeet / 100).toInt() * 100
            }

            // Format to tens of feet
            else if (distanceInFeet > 50)  {
                formattedDistance = ceil(distanceInFeet / 10).toInt() * 10
            }

            // Format to singles of feet
            else {
                formattedDistance = ceil(distanceInFeet).toInt()
            }

            formattedText += "$formattedDistance feet away"
        }

        distanceToDestinationTextView.text = formattedText
    }
}
