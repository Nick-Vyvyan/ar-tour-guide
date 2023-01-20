package com.example.artourguideapp.navigation

import android.location.Location
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.artourguideapp.entities.Entity
import com.google.ar.core.Anchor
import com.google.ar.core.TrackingState
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.ArSceneView
import com.google.ar.sceneform.math.Vector3
import java.util.*
import kotlin.math.min

/**
 * This class handles AR Navigation. MUST CALL init(...) BEFORE USE
 *
 * To use:
 *      call Navigation.init(arSceneView, activity)
 *      call startNavigationTo(entity) or stopNavigation()
 */
class Navigation private constructor(private var arSceneView: ArSceneView,
                                     private var activity: AppCompatActivity){

    /** Companion object so that Navigation can be started or stopped from anywhere */
    companion object {

        private lateinit var navigation: Navigation // Navigation object
        private lateinit var navButton: Button // Button on ArActivity

        /**
         * Initializes the navigation object and navButton
         */
        fun init(arSceneView: ArSceneView, activity: AppCompatActivity, navButton: Button) {
            navigation = Navigation(arSceneView, activity)
            Companion.navButton = navButton
        }

        /**
         * Starts navigation to a destination
         */
        fun startNavigationTo(destination: Entity) {
            if (navigationInitialized()) {
                navigation.navigateTo(destination)
                navButton.visibility = View.VISIBLE
                Log.d("NAVIGATION", "Nav button is visible")
            }
        }

        /**
         * Stops navigation
         */
        fun stopNavigation() {
            if (navigationInitialized()) {
                navigation.stopNavigation()
            }
        }

        private fun navigationInitialized(): Boolean {
            return navigation != null
        }
    }

    /** Timer Variables */
    private var navigationUpdateTimer: Timer = Timer()
    private val NAVIGATION_UPDATE_INTERVAL: Long = 2000

    /** Path Variables */
    private var destination: Entity? = null
    private var path: NavigationPath = NavigationPath(mutableListOf(), 0f)
    private val USER_REACHED_DESTINATION_RADIUS = 5f

    /** Waypoint Variables */
    private var currentWaypointIndex = 0
    private var currentWaypointAnchorNode: AnchorNode? = null
    private var nextWaypointAnchorNode: AnchorNode? = null
    private val UPDATE_WAYPOINT_RADIUS = 4f
    private val WAYPOINT_NODE_DISPLACEMENT = 2f

    /** AR Rendered Nodes */
    private var currentWaypointNode: NavigationWaypointNode = NavigationWaypointNode(activity)
    private var navigationArrowNode: NavigationArrowNode = NavigationArrowNode(activity, currentWaypointNode)

    // region Navigation

    /** Navigation entry function
     *  - stops any existing navigation
     *  - sets the new destination
     *  - generates a path to that destination
     *  - starts navigation updates
     */
    private fun navigateTo(newDestination: Entity) {
        stopNavigation()
        setDestination(newDestination)
        generatePath()
        startNavigationUpdates()
    }

    /** Stop Navigation */
    private fun stopNavigation() {
        // Stop navigation updates
        navigationUpdateTimer.cancel()

        // clear destination entity
        destination?.clearAsDestination()
        destination = null

        // Disable navigation arrow
        navigationArrowNode.isEnabled = false

        // Remove current waypoint node parent and detach anchor
        currentWaypointNode.parent = null
        currentWaypointAnchorNode?.anchor?.detach()

        // Detach next waypoint anchor
        nextWaypointAnchorNode?.anchor?.detach()

        // Make button invisible
        navButton.visibility = View.INVISIBLE
    }

    /** Set entity as destination on entity and on this*/
    private fun setDestination(newDestination: Entity) {
        newDestination.setAsDestination()
        destination = newDestination
    }

    /** Generate a path */
    private fun generatePath() {
        // If there is a destination
        if (destination != null) {

            // Clear existing path
            path.waypoints.clear()

            // Get AR Earth
            val earth = arSceneView.session?.earth
            if (earth?.trackingState == TrackingState.TRACKING) {

                // Get user location
                val userLocation = Location("User")
                userLocation.latitude = earth.cameraGeospatialPose.latitude
                userLocation.longitude = earth.cameraGeospatialPose.longitude

                // Get the path from the user to the destination
                path = PathFactory.getPathFromTo(userLocation, destination!!.getCentralLocation())
                currentWaypointIndex = 0

                // Create anchor for first waypoint
                val currentAnchor = earth.createAnchor(
                    path.waypoints[0].latitude,
                    path.waypoints[0].longitude,
                    earth.cameraGeospatialPose.altitude + WAYPOINT_NODE_DISPLACEMENT,
                    0f, 0f, 0f, 1f
                )


                // If only one waypoint in path, next index is 0, otherwise, 1
                val nextWaypointIndex = min(path.waypoints.size - 1, 1)

                // Create anchor for next waypoint
                val nextAnchor = earth.createAnchor(
                    path.waypoints[nextWaypointIndex].latitude,
                    path.waypoints[nextWaypointIndex].longitude,
                    earth.cameraGeospatialPose.altitude + WAYPOINT_NODE_DISPLACEMENT,
                    0f, 0f, 0f, 1f
                )

                // Create anchor nodes from anchors
                currentWaypointAnchorNode = AnchorNode(currentAnchor)
                nextWaypointAnchorNode = AnchorNode(nextAnchor)

                // Set anchor node parents to the AR Scene
                currentWaypointAnchorNode!!.parent = arSceneView.scene
                nextWaypointAnchorNode!!.parent = arSceneView.scene

                // Set current waypoint node parent
                currentWaypointNode.parent = currentWaypointAnchorNode
                currentWaypointNode.localPosition = Vector3(0f,0f,0f)
                currentWaypointNode.isEnabled = true

                // Set navigation arrow parent to the user and set its fields then enable
                navigationArrowNode.parent = arSceneView.scene.camera
                navigationArrowNode.currentWaypoint = currentWaypointNode
                navigationArrowNode.distanceFromCurrentWaypointToDestinationInMeters = path.distanceInMeters
                navigationArrowNode.isEnabled = true

            }
        }
    }

    /** Create a new navigation update timer and start navigation updates */
    private fun startNavigationUpdates() {
        navigationUpdateTimer = Timer()
        navigationUpdateTimer.schedule(object: TimerTask() {
            override fun run() {
                navigationUpdate()
            }
        },0, NAVIGATION_UPDATE_INTERVAL)
    }

    /** Navigation update */
    private fun navigationUpdate() {
        activity.runOnUiThread {

            // If path hasn't been generated, generate a path
            if(path.waypoints.isEmpty()) {
                generatePath()
            }

            // Otherwise continue navigation as usual
            else {
                if (!navigationArrowNode.isEnabled) {
                    navigationArrowNode.isEnabled = true
                }

                // Advance waypoints if needed
                if (shouldAdvanceToNextWaypoint()) {
                    advanceToNextWaypoint()
                }

                // If destination reached, stop navigation
                if (shouldStopNavigation()) {
                    stopNavigation()
                }
            }
        }
    }

    /** Check if user should stop navigation (i.e. destination reached) */
    private fun shouldStopNavigation(): Boolean {
        var shouldStopNavigation = false
        if (path.waypoints.isNotEmpty()) {

            // If current waypoint is the end of the path
            shouldStopNavigation = destinationReached()
        }
        return shouldStopNavigation
    }

    /** True if current waypoint is destination and user is within destination reached radius */
    private fun destinationReached(): Boolean {
        // If current waypoint is the end of the path
        if (currentWaypointIndex == path.waypoints.size - 1 && currentWaypointAnchorNode != null) {
            val distanceToDestination = Vector3.subtract(currentWaypointAnchorNode!!.worldPosition, arSceneView.scene.camera.worldPosition).length()
            if (distanceToDestination < USER_REACHED_DESTINATION_RADIUS) {
                return true
            }
        }
        return false
    }

    //endregion

    //region Waypoint Functions

    private fun shouldAdvanceToNextWaypoint(): Boolean {
        if (currentWaypointAnchorNode != null) {
            // Check if user is within a radius of the current waypoint
            val distanceFromUserToCurrentWaypoint = Vector3.subtract(currentWaypointAnchorNode!!.worldPosition, arSceneView.scene.camera.worldPosition).length()
            val userIsCloseEnoughForNewWaypoint = distanceFromUserToCurrentWaypoint < UPDATE_WAYPOINT_RADIUS

            return userIsCloseEnoughForNewWaypoint
        }

        return false

    }

    private fun advanceToNextWaypoint() {

        currentWaypointAnchorNode?.anchor?.detach() // detach current waypoint anchor
        currentWaypointAnchorNode = nextWaypointAnchorNode // assign current waypoint to next waypoint


        navigationArrowNode.currentWaypoint = currentWaypointNode

        // If current waypoint is destination, remove waypoint arrow
        if (currentWaypointIndex == path.waypoints.size - 1) {
            navigationArrowNode.distanceFromCurrentWaypointToDestinationInMeters = 0f
            currentWaypointNode.parent = null
        }
        else {
            navigationArrowNode.distanceFromCurrentWaypointToDestinationInMeters -= path.waypoints[currentWaypointIndex].distanceTo(path.waypoints[currentWaypointIndex + 1])
            currentWaypointNode.parent = currentWaypointAnchorNode
        }

        currentWaypointNode.localPosition = Vector3(0f,0f,0f)

        currentWaypointIndex++ // increase currentWaypointIndex by 1 (points to new current)

        // If next waypoint is not the destination, create an anchor for the next waypoint
        if (currentWaypointIndex < path.waypoints.size - 1) {
            val earth = arSceneView.session?.earth
            if (earth?.trackingState == TrackingState.TRACKING) {

                // Create anchor for next waypoint
                val nextAnchor = earth.createAnchor(
                    path.waypoints[currentWaypointIndex + 1].latitude,
                    path.waypoints[currentWaypointIndex + 1].longitude,
                    earth.cameraGeospatialPose.altitude + WAYPOINT_NODE_DISPLACEMENT,
                    0f, 0f, 0f, 1f
                )

                // Create anchor nodes from anchor
                nextWaypointAnchorNode = AnchorNode(nextAnchor)

                // Set anchor node parent to the AR Scene
                nextWaypointAnchorNode!!.parent = arSceneView.scene
            }
        }

        // If current waypoint is destination, disable waypoint node
        else {
            currentWaypointNode.isEnabled = false
        }
    }
    //endregion
}