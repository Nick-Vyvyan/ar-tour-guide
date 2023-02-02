package com.example.artourguideapp.navigation

import android.location.Location
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.artourguideapp.R
import com.example.artourguideapp.entities.Entity
import com.google.android.gms.maps.model.LatLng
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
                                     private var activity: AppCompatActivity){

    // 5E:1A:E4:6D:1A:F3:5B:72:6A:10:ED:45:24:D3:99:C2:FE:F4:35:F7

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
    private var pathLocations = mutableListOf<LatLng>()
    private val USER_REACHED_DESTINATION_RADIUS = 5f

    /** Waypoint Variables */
    private var currentWaypointIndex = 0
    private var currentWaypointAnchorNode: AnchorNode? = null
    private var nextWaypointAnchorNode: AnchorNode? = null
    private val UPDATE_WAYPOINT_RADIUS = 20f
    private val DESTINATION_LOCAL_SCALE = Vector3(.5f, .5f, .5f)
    private val DESTINATION_LOCAL_POSITION = Vector3(0f, .4f, 0f)

    /** Directions Variables */
    private var totalDistance = 0.0
    private var copyrights = ""

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
        generateDirectionsToDestination()
        startNavigationUpdates()
    }

    /** Set entity as destination on entity and on this*/
    private fun setDestination(newDestination: Entity) {
        newDestination.setAsDestination()
        destination = newDestination
    }

    /** Generate the directions to the current destination */
    private fun generateDirectionsToDestination() {
        thread {
            // Get AR Earth
            val earth = arSceneView.session?.earth
            if (earth?.trackingState == TrackingState.TRACKING) {

                // Generate a google maps JSON request
                val originString = "${earth.cameraGeospatialPose.latitude},${earth.cameraGeospatialPose.longitude}"
                val destinationString = "${destination!!.getCentralLocation().latitude},${destination!!.getCentralLocation().longitude}"
                val directionsRequestURL = "https://maps.googleapis.com/maps/api/directions/json?" +
                                            "origin=$originString&" +
                                            "destination=$destinationString&" +
                                            "key=${activity.resources.getString(R.string.GoogleMapsApiKey)}"
                val url = URL(directionsRequestURL)
                val connection = url.openConnection() as HttpURLConnection

                // Get the directions JSON Object
                val inputStream = connection.inputStream
                val directionsObject = JSONObject(inputStream.bufferedReader().readText())

                // Generate the path
                generatePathFromJSON(directionsObject)
            }
        }
    }

    /** Generate the path given from the Google Maps JSONObject */
    private fun generatePathFromJSON(directions: JSONObject) {

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

                // Add route start
                val routeStartLocation = defaultRouteLeg.getJSONObject("start_location")
                val routeStartLatLng = LatLng(routeStartLocation.getDouble("lat"), routeStartLocation.getDouble("lng"))
                pathLocations.add(routeStartLatLng)

                // Get the steps for that leg
                val steps = defaultRouteLeg.getJSONArray("steps")

                // Add each step location into the path locations
                for (i in 0 until steps.length()) {
                    val step = steps.getJSONObject(i)
                    val stepEndLocation = step.getJSONObject("end_location")
                    val pathLocation = LatLng(stepEndLocation.getDouble("lat"), stepEndLocation.getDouble("lng"))
                    pathLocations.add(pathLocation)
                }

                // Add route end
                val routeEndLocation = defaultRouteLeg.getJSONObject("end_location")
                val routeEndLatLng = LatLng(routeEndLocation.getDouble("lat"), routeEndLocation.getDouble("lng"))
                pathLocations.add(routeEndLatLng)

                // Get the total distance of the route in meters
                totalDistance = defaultRouteLeg.getJSONObject("distance").getDouble("value")

                // Get Google copyrights
                copyrights = defaultRoute.getString("copyrights")
            }

        }
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
        },0, NAVIGATION_UPDATE_INTERVAL)
    }

    /** Navigation update */
    private fun navigationUpdate() {
        activity.runOnUiThread {

            // If path hasn't been generated, generate a path
            if(pathLocations.isEmpty()) {
                generateDirectionsToDestination()
            }

            // Otherwise continue navigation as usual
            else {
                // Ensure that waypoint anchors are attached
                if (currentWaypointAnchorNode == null || nextWaypointAnchorNode == null) {
                    initializeWaypointAnchors()

                    // After waypoint anchor are initialized, initialize navigation arrow
                    if (!navigationArrowNode.isEnabled) {
                        initializeNavigationArrow()
                    }
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

    private fun initializeNavigationArrow() {
        // Set navigation arrow parent to the user and set its fields then enable
        navigationArrowNode.parent = arSceneView.scene.camera
        navigationArrowNode.currentWaypoint = currentWaypointNode
        navigationArrowNode.distanceFromCurrentWaypointToDestinationInMeters =
            totalDistance
        navigationArrowNode.isEnabled = true
    }

    private fun initializeWaypointAnchors() {
        // Get AR Earth
        val earth = arSceneView.session?.earth
        if (earth?.trackingState == TrackingState.TRACKING) {

            currentWaypointIndex = 0
            // Create anchor for first waypoint
            val currentAnchor = earth.createAnchor(
                pathLocations[currentWaypointIndex].latitude,
                pathLocations[currentWaypointIndex].longitude,
                earth.cameraGeospatialPose.altitude,
                0f, 0f, 0f, 1f
            )


            // If only one waypoint in path, next index is 0, otherwise, 1
            val nextWaypointIndex = min(pathLocations.size - 1, 1)

            // Create anchor for next waypoint
            val nextAnchor = earth.createAnchor(
                pathLocations[nextWaypointIndex].latitude,
                pathLocations[nextWaypointIndex].longitude,
                earth.cameraGeospatialPose.altitude,
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
            currentWaypointNode.localPosition = Vector3(0f, 0f, 0f)
            currentWaypointNode.isEnabled = true
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
            val distanceFromUserToCurrentWaypoint = Vector3.subtract(currentWaypointAnchorNode!!.worldPosition, arSceneView.scene.camera.worldPosition).length()
            val userIsCloseEnoughForNewWaypoint = distanceFromUserToCurrentWaypoint < UPDATE_WAYPOINT_RADIUS

            return userIsCloseEnoughForNewWaypoint
        }

        return false
    }

    private fun advanceToNextWaypoint() {
        currentWaypointAnchorNode?.anchor?.detach() // detach current waypoint anchor
        currentWaypointAnchorNode = nextWaypointAnchorNode // assign current waypoint to next waypoint

        // If current waypoint is destination, move waypoint arrow above destination
        if (currentWaypointIndex >= pathLocations.size - 1) {
            currentWaypointNode.parent = destination!!.getNode()
            currentWaypointNode.localPosition = DESTINATION_LOCAL_POSITION
            currentWaypointNode.localScale = DESTINATION_LOCAL_SCALE

            navigationArrowNode.distanceFromCurrentWaypointToDestinationInMeters = 0.0
            navigationArrowNode.currentWaypoint = destination!!.getNode()!!
        }
        else {
            // Subtract distance between waypoints from total distance
            var distanceResults = floatArrayOf(0f)
            Location.distanceBetween(pathLocations[currentWaypointIndex].latitude, pathLocations[currentWaypointIndex].longitude,
                                                                                     pathLocations[currentWaypointIndex + 1].latitude, pathLocations[currentWaypointIndex + 1].longitude,
                                                                                     distanceResults)
            navigationArrowNode.distanceFromCurrentWaypointToDestinationInMeters -= distanceResults[0]
            currentWaypointNode.parent = currentWaypointAnchorNode


            currentWaypointNode.localPosition = Vector3(0f,0f,0f)
            currentWaypointIndex++ // increase currentWaypointIndex by 1 (points to new current)

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
    }
    //endregion


}