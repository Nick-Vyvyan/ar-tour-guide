package com.example.artourguideapp.navigation

import android.location.Location
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.artourguideapp.R
import com.example.artourguideapp.entities.Entity
import com.example.artourguideapp.AnchorHelper
import com.google.android.gms.maps.model.LatLng
import com.google.ar.core.Earth
import com.google.ar.core.TrackingState
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.ArSceneView
import com.google.ar.sceneform.math.Vector3
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.concurrent.thread
import kotlin.math.min


/**
 * This class handles AR Navigation. MUST CALL init(...) BEFORE USE
 *
 * To use:
 *      call Navigation.init(arSceneView, activity)
 *      call startNavigationTo(entity) or stopNavigation()
 */
class Navigation private constructor(private var arSceneView: ArSceneView,
                                     private var activity: AppCompatActivity,
                                     private var navButton: Button){

    // 5E:1A:E4:6D:1A:F3:5B:72:6A:10:ED:45:24:D3:99:C2:FE:F4:35:F7

    /** Companion object so that Navigation can be started or stopped from anywhere */
    companion object {

        private lateinit var navigation: Navigation // Navigation object

        /**
         * Initializes the navigation object and navButton
         */
        fun init(arSceneView: ArSceneView, activity: AppCompatActivity) {
            navigation = Navigation(arSceneView, activity, activity.findViewById(R.id.stopNavButton))
        }

        /**
         * Starts navigation to a destination
         */
        fun startNavigationTo(destination: Entity) {
            if (navigationInitialized()) {
                navigation.navigateTo(destination)
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
    private val NAVIGATION_UPDATE_DELAY: Long = 2000

    /** Path Variables */
    private var destination: Entity? = null
    private var pathLocations = mutableListOf<LatLng>()
    private var pathDistanceRemainingFromWaypoint = mutableListOf<Double>()
    private val USER_REACHED_DESTINATION_RADIUS = 10f

    /** Waypoint Variables */
    private var currentWaypointIndex = 0
    private var currentWaypointAnchorNode: AnchorNode? = null
    private var nextWaypointAnchorNode: AnchorNode? = null
    private val UPDATE_WAYPOINT_RADIUS = 10f
    private val DESTINATION_LOCAL_SCALE = Vector3(.5f, .5f, .5f)
    private val DESTINATION_LOCAL_POSITION = Vector3(0f, .4f, 0f)

    /** Directions Variables */
    private var totalDistance = 0.0
    private var copyrights = ""

    /** AR Rendered Nodes */
    private var currentWaypointNode: NavigationWaypointNode = NavigationWaypointNode(activity)
    private var navigationArrowNode: NavigationArrowNode = NavigationArrowNode(activity, currentWaypointNode, "")

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
        generatePathAndUI()
        startNavigationUpdates()

        // If the user is not on tour, display the navigation button
        if (!Tour.onTour) {
            navButton.visibility = View.VISIBLE
        }
    }

    /** Set entity as destination on entity and on this*/
    private fun setDestination(newDestination: Entity) {
        newDestination.setAsDestination()
        destination = newDestination
        navigationArrowNode.destinationName = newDestination.getName()
    }

    /** Generates the path and UI. Done together since both tasks require AR Earth and therefore
     * appear only after AR Earth is successfully tracked.
     */
    private fun generatePathAndUI() {
        thread {
            val earth = arSceneView.session?.earth
            if (earth?.trackingState == TrackingState.TRACKING) {
                generateDirectionsToDestination(earth)
                activity.runOnUiThread {
                    initializeNavigationUI(earth)
                }
            }
        }
    }

    /** Generate the directions to the current destination */
    private fun generateDirectionsToDestination(earth: Earth) {
        // Generate a google maps JSON request
        val originString = "${earth.cameraGeospatialPose.latitude},${earth.cameraGeospatialPose.longitude}"
        val destinationString = "${destination!!.getCentralLocation().latitude},${destination!!.getCentralLocation().longitude}"
        val directionsRequestURL = "https://maps.googleapis.com/maps/api/directions/json?" +
                                    "origin=$originString&" +
                                    "destination=$destinationString&" +
                                    "mode=walking&" +
                                    "key=${activity.resources.getString(R.string.GoogleMapsApiKey)}"
        val url = URL(directionsRequestURL)
        val connection = url.openConnection() as HttpURLConnection

        // Get the directions JSON Object
        val inputStream = connection.inputStream
        val directionsObject = JSONObject(inputStream.bufferedReader().readText())

        // Generate the path
        generatePathFromJSON(directionsObject)
    }

    /** Generate the path given from the Google Maps JSONObject */
    private fun generatePathFromJSON(directions: JSONObject) {
        pathLocations.clear()
        pathDistanceRemainingFromWaypoint.clear()

        // Check route status
        val status = directions.getString("status")
        if (status == "OK") {
            // Get routes object
            val routes = directions.getJSONArray("routes")

            // If the default route exists, get it
            if (directions.length() > 0) {
                val defaultRoute = routes.getJSONObject(0)

                // Get the legs of the journey (should only be one leg since navigation is only between two locations)
                val defaultRouteLeg = defaultRoute.getJSONArray("legs").getJSONObject(0)

                // Get the total distance of the route in meters
                totalDistance = defaultRouteLeg.getJSONObject("distance").getDouble("value")

                var distanceRemaining = totalDistance

                // Get the steps for that leg
                val steps = defaultRouteLeg.getJSONArray("steps")

                // Add each step location into the path locations
                for (i in 0 until steps.length()) {
                    val step = steps.getJSONObject(i)
                    val stepEndLocation = step.getJSONObject("end_location")
                    val pathLocation = LatLng(stepEndLocation.getDouble("lat"), stepEndLocation.getDouble("lng"))
                    pathLocations.add(pathLocation)

                    val stepDistance = step.getJSONObject("distance").getDouble("value")
                    distanceRemaining -= stepDistance

                    pathDistanceRemainingFromWaypoint.add(distanceRemaining)
                }
                Log.d("NAVIGATION", "Path to ${destination!!.getName()} = $pathLocations")
                // Reset current waypoint index to beginning
                currentWaypointIndex = 0

                // Get Google copyrights
                copyrights = defaultRoute.getString("copyrights")
            }

        }
    }

    /**
     * Initializes the waypoint anchors, the navigation arrow, then points the
     * arrow to the correct node.
     */
    private fun initializeNavigationUI(earth: Earth) {
        initializeWaypointAnchorsAndNodes(earth)
        initializeNavigationArrow()
        pointArrowToCorrectNode()
    }

    /**
     * Initialize the waypoint anchors and nodes. Current anchor and next anchor will point to same
     * thing if there is only one location in pathLocations. Current waypoint node is enabled
     */
    private fun initializeWaypointAnchorsAndNodes(earth: Earth) {

        // Create anchor for first waypoint
        val currentAnchor = earth.createAnchor(
            pathLocations[currentWaypointIndex].latitude,
            pathLocations[currentWaypointIndex].longitude,
            earth.cameraGeospatialPose.altitude,
            0f, 0f, 0f, 1f
        )

        // Create anchor node, assign to scene
        currentWaypointAnchorNode = AnchorNode(currentAnchor)
        currentWaypointAnchorNode!!.parent = arSceneView.scene

        // set currentWaypointNode parent created anchor and enable the node
        currentWaypointNode.parent = currentWaypointAnchorNode
        currentWaypointNode.isEnabled = true


        // nextWaypointIndex = 0 if only one waypoint in path, 1 otherwise
        val nextWaypointIndex = min(currentWaypointIndex + 1, pathLocations.size - 1)

        // If the current waypoint is not the last waypoint
        if (currentWaypointIndex < pathLocations.size - 1) {

            // Create anchor for next waypoint
            val nextAnchor = earth.createAnchor(
                pathLocations[nextWaypointIndex].latitude,
                pathLocations[nextWaypointIndex].longitude,
                earth.cameraGeospatialPose.altitude,
                0f, 0f, 0f, 1f
            )

            // Create anchor node and assign parent to scene
            nextWaypointAnchorNode = AnchorNode(nextAnchor)
            nextWaypointAnchorNode!!.parent = arSceneView.scene
        }

        // Otherwise, point current and next anchor nodes to same thing
        else {
            nextWaypointAnchorNode = currentWaypointAnchorNode
        }
    }

    /**
     * If the destination node is active (button can be seen), arrow will point directly to destination
     * node and current waypoint will be disabled. Otherwise, enable and point to current waypoint
     */
    private fun pointArrowToCorrectNode() {
        navigationArrowNode.isEnabled = true

        val distanceFromUserToDestination = Vector3.subtract(destination!!.getNode().worldPosition, arSceneView.scene.camera.worldPosition).length()

        // If destination node is visible, point directly to the destination, otherwise, point to the current waypoint node
        if (destination!!.getNode()!!.isActive &&
            distanceFromUserToDestination > USER_REACHED_DESTINATION_RADIUS &&
            distanceFromUserToDestination < AnchorHelper.VISIBLE_NODE_PROXIMITY_DISTANCE) {
            Log.d("NAVIGATION", "Arrow pointing to destination")
            navigationArrowNode.currentWaypoint = destination!!.getNode()
            navigationArrowNode.distanceFromCurrentWaypointToDestinationInMeters = 0.0
            currentWaypointNode.isEnabled = false
        }
        else {
            navigationArrowNode.currentWaypoint = currentWaypointNode
            navigationArrowNode.distanceFromCurrentWaypointToDestinationInMeters = pathDistanceRemainingFromWaypoint[currentWaypointIndex]
            currentWaypointNode.isEnabled = true
        }
    }

    /**
     * Initialize the navigation arrow. Set the parent to the user, set the distance to the total
     * distance, and enable the arrow
     */
    private fun initializeNavigationArrow() {
        // Set navigation arrow parent to the user and set its fields then enable
        navigationArrowNode.parent = arSceneView.scene.camera
        navigationArrowNode.distanceFromCurrentWaypointToDestinationInMeters =
            totalDistance
        navigationArrowNode.isEnabled = true
    }

    /** Stop Navigation */
    private fun stopNavigation() {
        // Stop navigation updates
        navigationUpdateTimer.cancel()

        // clear destination entity
        destination?.clearAsDestination()
        destination = null

        // Disable navigation arrow and reset distance
        navigationArrowNode.isEnabled = false
        navigationArrowNode.distanceFromCurrentWaypointToDestinationInMeters = 0.0

        // Remove current waypoint node parent and detach anchor
        currentWaypointNode.parent = null
        currentWaypointAnchorNode?.anchor?.detach()
        currentWaypointAnchorNode = null

        // Detach next waypoint anchor
        nextWaypointAnchorNode?.anchor?.detach()
        nextWaypointAnchorNode = null

        // Make button invisible
        navButton.visibility = View.INVISIBLE
    }

    /** Create a new navigation update timer and start navigation updates */
    private fun startNavigationUpdates() {
        navigationUpdateTimer = Timer()
        navigationUpdateTimer.schedule(object: TimerTask() {
            override fun run() {
                navigationUpdate()
            }
        },NAVIGATION_UPDATE_DELAY, NAVIGATION_UPDATE_INTERVAL)
    }

    /** Navigation update */
    private fun navigationUpdate() {
        // If path hasn't been generated, generate the path and UI
        if(pathLocations.isEmpty()) {
            generatePathAndUI()
        }

        // Otherwise continue navigation as usual
        else {
            activity.runOnUiThread {
                if (currentWaypointAnchorNode == null) {
                    val earth = arSceneView.session?.earth
                    if (earth?.trackingState == TrackingState.TRACKING) {
                        initializeWaypointAnchorsAndNodes(earth)
                    }
                }

                // Advance waypoints if needed
                if (shouldAdvanceToNextWaypoint()) {
                    advanceToNextWaypoint()
                }

                pointArrowToCorrectNode()

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
        if (pathLocations.isNotEmpty()) {

            // If current waypoint is the end of the path
            shouldStopNavigation = destinationReached()
        }
        return shouldStopNavigation
    }

    /** True if current waypoint is destination and user is within destination reached radius */
    private fun destinationReached(): Boolean {
        // If current waypoint is the end of the path
        if (currentWaypointIndex == pathLocations.size - 1 && currentWaypointAnchorNode != null) {
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
            val userIsCloseEnoughToCurrentWaypoint = navigationArrowNode.distanceToWaypoint < UPDATE_WAYPOINT_RADIUS

            // Make sure that the current waypoint isn't the end of the path
            val currentIndexIsNotFinalIndex = currentWaypointIndex < pathLocations.size - 1

            var userIsCloserToNewWaypointThanCurrent = false
            // Determine if distance from user to next waypoint is less than current waypoint
            if (currentWaypointAnchorNode != nextWaypointAnchorNode) {
                val distanceFromUserToNextWaypoint = Vector3.subtract(nextWaypointAnchorNode!!.worldPosition, arSceneView.scene.camera.worldPosition).length()
                val distanceFromCurrentToNextWaypoint = Vector3.subtract(nextWaypointAnchorNode!!.worldPosition, currentWaypointAnchorNode!!.worldPosition).length()
                userIsCloserToNewWaypointThanCurrent = distanceFromCurrentToNextWaypoint > distanceFromUserToNextWaypoint
            }

            Log.d("NAVIGATION", "Closer to new than current = $userIsCloserToNewWaypointThanCurrent\nClose enough to current = $userIsCloseEnoughToCurrentWaypoint")
            return (userIsCloserToNewWaypointThanCurrent || userIsCloseEnoughToCurrentWaypoint) && currentIndexIsNotFinalIndex
        }

        return false
    }

    private fun advanceToNextWaypoint() {
        Log.d("NAVIGATION", "Advancing to next waypoint")
        currentWaypointAnchorNode?.anchor?.detach() // detach current waypoint anchor
        currentWaypointAnchorNode = nextWaypointAnchorNode // assign current waypoint to next waypoint

        currentWaypointNode.parent = currentWaypointAnchorNode
        currentWaypointNode.localPosition = Vector3(0f,0f,0f)

        currentWaypointIndex++ // increase currentWaypointIndex by 1 (points to new current)

        navigationArrowNode.distanceFromCurrentWaypointToDestinationInMeters = pathDistanceRemainingFromWaypoint[currentWaypointIndex]
        // If next waypoint is not the destination, create an anchor for the next waypoint
        if (currentWaypointIndex < pathLocations.size - 1) {
            val earth = arSceneView.session?.earth
            if (earth?.trackingState == TrackingState.TRACKING) {

                // Create anchor for next waypoint
                val nextAnchor = earth.createAnchor(
                    pathLocations[currentWaypointIndex + 1].latitude,
                    pathLocations[currentWaypointIndex + 1].longitude,
                    earth.cameraGeospatialPose.altitude,
                    0f, 0f, 0f, 1f
                )

                // Create anchor nodes from anchor
                nextWaypointAnchorNode = AnchorNode(nextAnchor)

                // Set anchor node parent to the AR Scene
                nextWaypointAnchorNode!!.parent = arSceneView.scene
            }
        }


    }
    //endregion


}