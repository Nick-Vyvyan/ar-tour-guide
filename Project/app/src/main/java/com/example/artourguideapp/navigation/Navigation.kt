package com.example.artourguideapp.navigation

import android.location.Location
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.artourguideapp.R
import com.example.artourguideapp.entities.Entity
import com.example.artourguideapp.AnchorHelper
import com.example.artourguideapp.AppSettings
import com.example.artourguideapp.ArSessionFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.floatingactionbutton.FloatingActionButton
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

        /**
         * Initializes the navigation object
         */
        fun init(arSceneView: ArSceneView, activity: AppCompatActivity) {
            navigation = Navigation(arSceneView, activity)
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

    /** Update Timer */
    private var navigationUpdateTimer: Timer = Timer()

    /** Destination and Waypoint Variables */
    private var destination: Entity? = null
    private var waypoints = mutableListOf<LatLng>()
    private var distancesRemainingFromWaypoint = mutableListOf<Double>()
    private var distanceFromCurrentWaypointToDestination = 0.0
    private var currentWaypointIndex = 0

    /** UI Elements */
    private var destinationLinearLayout: LinearLayout = activity.findViewById(R.id.destinationLinearLayout)
    private var navigationTypeTextView: TextView = activity.findViewById(R.id.navigationTypeTextView)
    private var destinationNameTextView: TextView = activity.findViewById(R.id.destinationNameTextView)
    private var stopNavButton: FloatingActionButton = activity.findViewById(R.id.stopNavButton)
    private var copyrightsTextView: TextView = activity.findViewById(R.id.copyrightsText)

    /** AR Elements */
    private var currentWaypointAnchorNode: AnchorNode? = null
    private var nextWaypointAnchorNode: AnchorNode? = null
    private var currentWaypointNode: NavigationWaypointNode = NavigationWaypointNode(activity)
    private var navigationArrowNode: NavigationArrowNode = NavigationArrowNode(activity, currentWaypointNode)

    /** Google Maps Licensing */
    private var copyrights = ""

    // region Navigation

    /** Navigation entry function
     *  - stops any existing navigation
     *  - sets the new destination
     *  - generates a path to that destination and displays UI
     *  - starts navigation updates
     */
    private fun navigateTo(newDestination: Entity) {
        stopNavigation()
        setDestination(newDestination)
        generatePathAndUI()
        startNavigationUpdates()
    }

    /** Set entity as destination on entity and on this */
    private fun setDestination(newDestination: Entity) {
        newDestination.setAsDestination()
        destination = newDestination
    }

    /** Stop Navigation and reset all variables */
    private fun stopNavigation() {
        // Stop navigation updates
        navigationUpdateTimer.cancel()

        // clear destination entity
        destination?.clearAsDestination()
        destination = null

        // Disable navigation arrow and reset distance
        navigationArrowNode.isEnabled = false
        navigationArrowNode.distanceFromCurrentWaypointToDestination = 0.0

        // Remove current waypoint node parent and detach anchor
        currentWaypointNode.parent = null
        currentWaypointAnchorNode?.anchor?.detach()
        currentWaypointAnchorNode = null

        // Detach next waypoint anchor
        nextWaypointAnchorNode?.anchor?.detach()
        nextWaypointAnchorNode = null

        // Clear path locations and distances
        waypoints.clear()
        distancesRemainingFromWaypoint.clear()

        // Reset UI
        if (!Tour.onTour) {
            resetUIElements()
        }
    }

    private fun resetUIElements() {
        stopNavButton.visibility = View.INVISIBLE

        destinationNameTextView.text = ""

        destinationLinearLayout.visibility = View.INVISIBLE

        copyrightsTextView.text = ""
        copyrightsTextView.visibility = View.INVISIBLE
    }

    //endregion

    //region Path and UI Generation

    /** Generates the path and UI. Done together since both tasks require AR Earth and therefore
     * UI elements and path elements appear together.
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
                distanceFromCurrentWaypointToDestination = defaultRouteLeg.getJSONObject("distance").getDouble("value")

                var distanceRemaining = distanceFromCurrentWaypointToDestination

                // Get the steps for that leg
                val steps = defaultRouteLeg.getJSONArray("steps")

                // Add each step location into the path locations
                for (i in 0 until steps.length()) {
                    val step = steps.getJSONObject(i)
                    val stepEndLocation = step.getJSONObject("start_location")
                    val pathLocation = LatLng(stepEndLocation.getDouble("lat"), stepEndLocation.getDouble("lng"))
                    waypoints.add(pathLocation)

                    val stepDistance = step.getJSONObject("distance").getDouble("value")
                    distanceRemaining -= stepDistance

                    distancesRemainingFromWaypoint.add(distanceRemaining)
                }
                Log.d("NAVIGATION", "Path to ${destination!!.getName()} = $waypoints")

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
        setUIElements()

    }

    /** Set all UI elements based on current data */
    private fun setUIElements() {
        // If the user is not on tour, display the navigation button
        if (!Tour.onTour) {
            stopNavButton.visibility = View.VISIBLE
            stopNavButton.setOnClickListener {
                Navigation.stopNavigation()
            }
            navigationTypeTextView.visibility = View.GONE
        }

        destinationNameTextView.text = destination!!.getName()

        destinationLinearLayout.visibility = View.VISIBLE

        copyrightsTextView.text = copyrights
        copyrightsTextView.visibility = View.VISIBLE
    }

    /**
     * Initialize the waypoint anchors and nodes. Current anchor and next anchor will point to same
     * thing if there is only one location in waypoints. Current waypoint node is enabled
     */
    private fun initializeWaypointAnchorsAndNodes(earth: Earth) {

        // If current waypoint index is in bounds
        if (currentWaypointIndex < waypoints.size) {

            // Create anchor for current waypoint
            val currentAnchor = earth.createAnchor(
                waypoints[currentWaypointIndex].latitude,
                waypoints[currentWaypointIndex].longitude,
                earth.cameraGeospatialPose.altitude,
                0f, 0f, 0f, 1f
            )

            // Create anchor node, assign to scene
            currentWaypointAnchorNode = AnchorNode(currentAnchor)
            currentWaypointAnchorNode!!.parent = arSceneView.scene

            // set currentWaypointNode parent to created anchor and enable the node
            currentWaypointNode.parent = currentWaypointAnchorNode
            currentWaypointNode.isEnabled = true

            // If the current waypoint is not the last waypoint
            if (currentWaypointIndex < waypoints.size - 1) {
                val nextWaypointIndex = currentWaypointIndex + 1

                // Create anchor for next waypoint
                val nextAnchor = earth.createAnchor(
                    waypoints[nextWaypointIndex].latitude,
                    waypoints[nextWaypointIndex].longitude,
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
    }

    /**
     * Initialize the navigation arrow. Set the parent to the user, set the distance to the total
     * distance, and enable the arrow
     */
    private fun initializeNavigationArrow() {
        // Set navigation arrow parent to the user and set its fields then enable
        navigationArrowNode.parent = arSceneView.scene.camera
        navigationArrowNode.distanceFromCurrentWaypointToDestination =
            distanceFromCurrentWaypointToDestination
        navigationArrowNode.isEnabled = true
    }

    //endregion

    //region Update Functions

    /** Create a new navigation update timer and start navigation updates */
    private fun startNavigationUpdates() {
        navigationUpdateTimer = Timer()
        navigationUpdateTimer.schedule(object: TimerTask() {
            override fun run() {
                navigationUpdate()
            }
        },AppSettings.NAVIGATION_UPDATE_DELAY, AppSettings.NAVIGATION_UPDATE_INTERVAL)
    }

    /** Navigation update */
    private fun navigationUpdate() {
        // If path hasn't been generated, generate the path and UI
        if(waypoints.isEmpty()) {
            generatePathAndUI()
        }

        // Otherwise continue navigation as usual
        else {
            activity.runOnUiThread {
                // If current waypoint anchor node is null, initialize the waypoint anchors
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

                // Update navigation arrow to point at correct node
                pointArrowToCorrectNode()

                // If navigation should be stopped, stop navigation
                if (shouldStopNavigation()) {
                    stopNavigation()
                }
            }
        }
    }

    /**
     * If the destination node is active (button can be seen), arrow will point directly to destination
     * node and current waypoint will be disabled. Otherwise, enable and point to current waypoint
     */
    private fun pointArrowToCorrectNode() {
        // If non-null destination and earth is being tracked
        if (destination != null && arSceneView.session?.earth?.trackingState == TrackingState.TRACKING) {
            val earth = arSceneView.session?.earth!!

            var userLocation = Location("User")
            userLocation.latitude = earth.cameraGeospatialPose.latitude
            userLocation.longitude = earth.cameraGeospatialPose.longitude

            // If destination is visible, point directly to it
            if (userLocation.distanceTo(destination!!.getCentralLocation()) < AppSettings.VISIBLE_NODE_PROXIMITY_DISTANCE) {
                navigationArrowNode.pointDirectlyToWaypoint = true
                navigationArrowNode.currentWaypoint = destination!!.getNode()
                navigationArrowNode.distanceFromCurrentWaypointToDestination = 0.0
                currentWaypointNode.isEnabled = false
            }

            // Otherwise point to current waypoint anchor node
            else if (currentWaypointAnchorNode != null) {
                navigationArrowNode.pointDirectlyToWaypoint = false
                navigationArrowNode.currentWaypoint = currentWaypointAnchorNode!!
                // Update distance remaining if the current waypoint index is in bounds
                if (currentWaypointIndex < distancesRemainingFromWaypoint.size) {
                    navigationArrowNode.distanceFromCurrentWaypointToDestination = distancesRemainingFromWaypoint[currentWaypointIndex]
                }
                currentWaypointNode.isEnabled = true
            }

        }
    }

    /**
     * Move waypoints forward. Point current waypoint to the next waypoint. Increment the index.
     * Create another waypoint anchor if necessary.
     */
    private fun advanceToNextWaypoint() {
        currentWaypointAnchorNode?.anchor?.detach() // detach current waypoint anchor
        currentWaypointAnchorNode = nextWaypointAnchorNode // assign current waypoint to next waypoint

        currentWaypointNode.parent = currentWaypointAnchorNode
        currentWaypointNode.localPosition = Vector3(0f,0f,0f)

        currentWaypointIndex++ // increase currentWaypointIndex by 1 (points to new current)

        // update arrow node distance
        navigationArrowNode.distanceFromCurrentWaypointToDestination = distancesRemainingFromWaypoint[currentWaypointIndex]

        // If next waypoint is not the destination, create an anchor for the next waypoint
        if (currentWaypointIndex < waypoints.size - 1) {
            val earth = arSceneView.session?.earth
            if (earth?.trackingState == TrackingState.TRACKING) {

                // Create anchor for next waypoint
                val nextAnchor = earth.createAnchor(
                    waypoints[currentWaypointIndex + 1].latitude,
                    waypoints[currentWaypointIndex + 1].longitude,
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

    //region Helper Boolean Functions
    /** Returns true if there is a generated path and the destination has been reached */
    private fun shouldStopNavigation(): Boolean {
        var shouldStopNavigation = false
        if (waypoints.isNotEmpty()) {

            // If current waypoint is the end of the path
            shouldStopNavigation = destinationReached()
        }
        return shouldStopNavigation
    }


    /** True if current waypoint is destination and user is within destination reached radius */
    private fun destinationReached(): Boolean {
        // If current waypoint is the destination and the distance is within the reached destination radius, return true
        return  navigationArrowNode.currentWaypoint == destination!!.getNode() &&
                navigationArrowNode.distanceToCurrentWaypoint < AppSettings.USER_WITHIN_DESTINATION_RADIUS
    }

    /**
     * Returns true if:
     *  1) current waypoint is not the final waypoint AND
     *  2a) user is closer to the next waypoint than the current waypoint is OR
     *  2b) user is within UPDATE_WAYPOINT_RADIUS meters away
     */
    private fun shouldAdvanceToNextWaypoint(): Boolean {
        if (currentWaypointAnchorNode != null && userPositionNotNull()) {

            // Check if user is within a radius of the current waypoint
            val userIsCloseEnoughToCurrentWaypoint = navigationArrowNode.distanceToCurrentWaypoint < AppSettings.USER_WITHIN_WAYPOINT_RADIUS

            // Make sure that the current waypoint isn't the final waypoint
            val currentWaypointIsNotFinalWaypoint = currentWaypointIndex < waypoints.size - 1

            // Determine if distance from user to next waypoint is less than current waypoint
            var userIsCloserToNewWaypointThanCurrent = false

            // Only check distance difference if current and next point to different things
            if (currentWaypointAnchorNode != nextWaypointAnchorNode) {
                val distanceFromUserToNextWaypoint = Vector3.subtract(nextWaypointAnchorNode!!.worldPosition, arSceneView.scene.camera.worldPosition).length()
                val distanceFromCurrentToNextWaypoint = Vector3.subtract(nextWaypointAnchorNode!!.worldPosition, currentWaypointAnchorNode!!.worldPosition).length()
                userIsCloserToNewWaypointThanCurrent = distanceFromCurrentToNextWaypoint > distanceFromUserToNextWaypoint
                Log.d("NAVIGATION", "distance from user to next = $distanceFromUserToNextWaypoint\ndistance from current to next = $distanceFromCurrentToNextWaypoint")

            }

            Log.d("NAVIGATION", "closer to next = $userIsCloserToNewWaypointThanCurrent\nclose enough to current = $userIsCloseEnoughToCurrentWaypoint")

            val shouldAdvanceToNextWaypoint = (userIsCloserToNewWaypointThanCurrent || userIsCloseEnoughToCurrentWaypoint) && currentWaypointIsNotFinalWaypoint

            return shouldAdvanceToNextWaypoint
        }

        return false
    }

    private fun userPositionNotNull(): Boolean {
        return  arSceneView != null &&
                arSceneView.scene != null &&
                arSceneView.scene.camera != null &&
                arSceneView.scene.camera.worldPosition != null
    }

    //endregion

}