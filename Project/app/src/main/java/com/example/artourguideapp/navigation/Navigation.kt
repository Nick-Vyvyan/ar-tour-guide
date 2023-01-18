package com.example.artourguideapp.navigation

import android.location.Location
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.example.artourguideapp.ArActivity
import com.example.artourguideapp.R
import com.example.artourguideapp.entities.Entity
import com.google.ar.core.Anchor
import com.google.ar.core.GeospatialPose
import com.google.ar.core.TrackingState
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.ArSceneView
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ViewRenderable
import java.util.*
import kotlin.math.*

/**
 * This class handles AR Navigation. MUST CALL init(...) BEFORE USE
 *
 * To use:
 *      call Navigation.init(arSceneView, activity)
 *      call startNavigationTo(entity) or stopNavigation()
 */
class Navigation private constructor(private var arSceneView: ArSceneView,
                                     private var activity: AppCompatActivity){

    // Companion object so that Navigation can be started or stopped from anywhere
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
                ArActivity.navigating = true
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

    // Variables related to arrow/"this way" sign
    private var arrowPlaced: Boolean = false
    private var arrowAnchor: Anchor? = null
    private var arrowNode: Node = Node()
    private var arrowUpdateTimer: Timer = Timer()

    // Path variables
    private var waypoints = Waypoints()
    private var path: MutableList<Location> = mutableListOf()
    private var pathIndex = 0
    private lateinit var destination: Entity
    private var distanceNode: Node = Node()
    private val distanceNodeScaleMultiplier = 0.5f
    private var currentWaypoint: AnchorNode? = null
    private var nextWaypoint: AnchorNode? = null


    private val ARROW_UPDATE_INTERVAL: Long = 2000
    private val ARROW_PLACEMENT_DISTANCE_MULTIPLIER = .00001
    private val ARROW_HEIGHT_DISPLACEMENT = -0.3f

    // Once object is constructed, start building the AR arrow
    init {
        buildArrow()
    }


    // region Navigation

    private fun navigateTo(newDestination: Entity) {
        setDestination(newDestination)
        generatePath()
        startNavigation()
    }

    // Set entity as destination
    private fun setDestination(newDestination: Entity) {
        newDestination.setAsDestination()
        destination = newDestination
    }

    // Generate a path and set waypoints
    private fun generatePath() {
        path.clear()

        val earth = arSceneView.session?.earth
        if (earth?.trackingState == TrackingState.TRACKING) {
            val userLocation = Location("User")
            userLocation.latitude = earth.cameraGeospatialPose.latitude
            userLocation.longitude = earth.cameraGeospatialPose.longitude

            path = waypoints.getPathFromTo(userLocation, destination.getCentralLocation())
            path.add(destination.getCentralLocation())
            pathIndex = 0
            Log.d("NAVIGATION", "Path = $path")

            // Create anchor for current waypoint
            val currentAnchor = earth.createAnchor(
                path[0].latitude,
                path[0].longitude,
                earth.cameraGeospatialPose.altitude,
                0f, 0f, 0f, 1f
            )

            var nextAnchor: Anchor

            if (path.size > 1) {
                // Create anchor for next waypoint
                nextAnchor = earth.createAnchor(
                    path[1].latitude,
                    path[1].longitude,
                    earth.cameraGeospatialPose.altitude,
                    0f, 0f, 0f, 1f
                )
            }
            else {
                // Create anchor for next waypoint
                nextAnchor = earth.createAnchor(
                    path[0].latitude,
                    path[0].longitude,
                    earth.cameraGeospatialPose.altitude,
                    0f, 0f, 0f, 1f
                )
            }

            // Create anchor nodes from anchors
            currentWaypoint = AnchorNode(currentAnchor)
            nextWaypoint = AnchorNode(nextAnchor)

            // Set anchor node parents to the AR Scene
            currentWaypoint!!.parent = arSceneView.scene
            nextWaypoint!!.parent = arSceneView.scene

            buildDistanceNode(path[0].distanceTo(destination.getCentralLocation()))
            distanceNode.parent = currentWaypoint
            updateDistanceNode()
            }
        }

    // Stop any existing navigation and schedule navigation updates
    private fun startNavigation() {
        stopNavigation()
        scheduleNavigationUpdates()
    }

    // Create new Timer and schedule updates
    private fun scheduleNavigationUpdates() {
        arrowUpdateTimer = Timer()
        arrowUpdateTimer.schedule(object: TimerTask() {
            override fun run() {
                navigationUpdate()
            }
        },0, ARROW_UPDATE_INTERVAL)
    }

    // Navigation update
    private fun navigationUpdate() {
        activity.runOnUiThread {
            if(path.isEmpty()) {
                generatePath()
            }
            else {
                if (shouldPlaceNewArrow()) {
                    placeNewArrow()
                } else {
                    setArrowRotation()
                }

                if (shouldUpdateWaypoints()) {
                    updateWaypoints()
                }
                else {
                    updateDistanceNode()
                }

//                if (shouldEndNavigation()) {
//                    stopNavigation()
//                }
            }
        }
    }

    // Stop navigation. Cancel nav updates. Clear destination. Detach anchors, set arrowNode parent to null
    private fun stopNavigation() {
        arrowUpdateTimer.cancel()
        destination.clearAsDestination()
        arrowNode.parent = null
        currentWaypoint?.anchor?.detach()
        nextWaypoint?.anchor?.detach()
        arrowAnchor?.detach()
        arrowPlaced = false
        navButton.visibility = View.INVISIBLE
    }

    private fun shouldEndNavigation(): Boolean {
        if (path.isNotEmpty()) {
            //return destination.getCentralLocation() == path[pathIndex]
        }

        return false
    }

    //endregion

    //region Arrow Functions

    // Create the AR arrow and store in arrowNode.renderable
    private fun buildArrow() {
        arrowNode.name = "Arrow"

        // Programmatically build an ar button without a XML
        val arButton = Button(activity)

        arButton.setTextColor(ResourcesCompat.getColor(activity.resources, R.color.wwu_white, null))
        arButton.setBackgroundResource(R.drawable.button_rounded_corners)
        arButton.setBackgroundColor(ResourcesCompat.getColor(activity.resources, R.color.wwu_blue, null))
        arButton.text = "This way"

        ViewRenderable.builder().setView(activity, arButton).build()
            .thenAccept {renderable ->
                arrowNode.renderable = renderable }
    }

    // Place a new arrow in front of the user
    private fun placeNewArrow() {

        // If an arrow already exists, remove it
        if (arrowPlaced) {
            arrowNode.parent = null
            arrowAnchor?.detach()
            arrowPlaced = false
        }

        // Get AR Earth
        val earth = arSceneView.session?.earth
        if (earth?.trackingState == TrackingState.TRACKING) {
            val userLocation = Location("User")
            userLocation.latitude = earth.cameraGeospatialPose.latitude
            userLocation.longitude = earth.cameraGeospatialPose.longitude

            // buildArrowWithDistance(userLocation.distanceTo(destination.getCentralLocation()) * 3.28084f)

            //TODO: Make arrow location point to next waypoint
            val arrowLocation = getArrowLocation(earth.cameraGeospatialPose)
            val arrowHeight = earth.cameraGeospatialPose.altitude + ARROW_HEIGHT_DISPLACEMENT

            // Create anchor at arrow location with arrow height
            arrowAnchor = earth.createAnchor(
                    arrowLocation.latitude,
                    arrowLocation.longitude,
                    arrowHeight,
                    0f, 0f, 0f, 1f
                )

            // Create anchor node from anchor
            val arAnchorNode = AnchorNode(arrowAnchor)

            // Set entity node parent to anchor node
            arrowNode.parent = arAnchorNode

            // Set anchor node parent to the AR Scene
            arAnchorNode.parent = arSceneView.scene

            // Rotate arrow
            setArrowRotation()

            arrowPlaced = true
        }
    }

    // Rotates arrow. Currently rotated toward user
    private fun setArrowRotation() {

        // Get the vector from the arrow to the user
        val direction = Vector3.subtract(arrowNode.worldPosition, arSceneView.scene.camera.worldPosition)

        // Updated rotation as Quaternion
        val lookRotation = Quaternion.lookRotation(direction, Vector3.up())

        arrowNode.worldRotation = lookRotation
    }

    private fun getArrowLocation(userGeospatialPose: GeospatialPose): Location {

        // Vector from user to destination
        val directionX = destination.getCentralLocation().latitude - userGeospatialPose.latitude
        val directionY = destination.getCentralLocation().longitude - userGeospatialPose.longitude

        // Normalize and scale that vector
        val magnitude = sqrt(directionX.pow(2) + directionY.pow(2))
        val normalizedX = directionX/magnitude
        val normalizedY = directionY/magnitude

        val scaledX = normalizedX * ARROW_PLACEMENT_DISTANCE_MULTIPLIER
        val scaledY = normalizedY * ARROW_PLACEMENT_DISTANCE_MULTIPLIER

        // Add that vector to the user location for arrow location
        var arrowLocation: Location = Location("Arrow")
        arrowLocation.latitude = userGeospatialPose.latitude + scaledX
        arrowLocation.longitude = userGeospatialPose.longitude + scaledY

        return arrowLocation

    }

    // If user is closer to the destination than previous arrow, true
    private fun shouldPlaceNewArrow(): Boolean {

        // Check if user is closer to destination than most recent arrow
        val distanceFromDestinationToUser = Vector3.subtract(destination.getNode().worldPosition, arSceneView.scene.camera.worldPosition).length()
        val distanceFromDestinationToArrow = Vector3.subtract(destination.getNode().worldPosition, arrowNode.worldPosition).length()
        val userIsCloserThanArrow = distanceFromDestinationToUser < distanceFromDestinationToArrow


        return !arrowPlaced || userIsCloserThanArrow
    }

    //endregion

    //region Waypoint Functions

    private fun buildDistanceNode(distanceInMeters: Float) {
        val distanceInFeet = distanceInMeters * 3.28084
        val formattedDistance = distanceInFeet.toInt()

        distanceNode.name = "Distance sign"

        // Programmatically build an ar button without a XML
        val arButton = Button(activity)

        arButton.setTextColor(ResourcesCompat.getColor(activity.resources, R.color.wwu_white, null))
        arButton.setBackgroundResource(R.drawable.button_rounded_corners)
        arButton.setBackgroundColor(ResourcesCompat.getColor(activity.resources,
            R.color.wwu_blue, null))
        arButton.text = "${destination.getName()}\n$formattedDistance feet"

        ViewRenderable.builder().setView(activity, arButton).build()
            .thenAccept {renderable ->
                distanceNode.renderable = renderable }
    }

    private fun updateDistanceNode() {
        // Get the vector from the arrow to the user
        val direction = Vector3.subtract(distanceNode.worldPosition, arSceneView.scene.camera.worldPosition)

        // Updated rotation as Quaternion
        val lookRotation = Quaternion.lookRotation(direction, Vector3.up())

        val scale = Vector3(direction.length() * distanceNodeScaleMultiplier,
                            direction.length() * distanceNodeScaleMultiplier,
                            direction.length() * distanceNodeScaleMultiplier)

        distanceNode.worldRotation = lookRotation
        distanceNode.worldScale = scale
        distanceNode.isEnabled = true

//        Log.d("NAVIGATION","User = ${arSceneView.scene.camera.worldPosition}" +
//                "\ndestination = ${destination.getNode().worldPosition}" +
//                "\ndistance sign = ${distanceNode.worldPosition}" +
//                "\ndistanceNode scale = ${distanceNode.worldScale}" +
//                "\ndistance node renderable = ${distanceNode.renderable}" +
//                "\ncurrent waypoint = ${currentWaypoint?.isActive}" +
//                "\ndistance node enabled = ${distanceNode.isEnabled}")
    }

    private fun shouldUpdateWaypoints(): Boolean {
        if (currentWaypoint != null) {
            // Check if user is closer to destination than most recent arrow
            val distanceFromDestinationToUser = Vector3.subtract(destination.getNode().worldPosition, arSceneView.scene.camera.worldPosition).length()
            val distanceFromDestinationToLastWaypoint = Vector3.subtract(destination.getNode().worldPosition, currentWaypoint?.worldPosition).length()
            val userIsCloserThanLastWaypoint = distanceFromDestinationToUser < distanceFromDestinationToLastWaypoint

            return userIsCloserThanLastWaypoint
        }

        return false

    }

    private fun updateWaypoints() {
        // detach current waypoint anchor
        // assign current waypoint to next waypoint
        // increase pathIndex by 1 (points to new current)
        currentWaypoint?.anchor?.detach()
        currentWaypoint = nextWaypoint
        pathIndex++

        val earth = arSceneView.session?.earth
        if (earth?.trackingState == TrackingState.TRACKING) {
            if (pathIndex < path.size - 1) {
                // Create anchor for next waypoint
                val nextAnchor = earth.createAnchor(
                    path[pathIndex + 1].latitude,
                    path[pathIndex + 1].longitude,
                    earth.cameraGeospatialPose.altitude,
                    0f, 0f, 0f, 1f
                )
                // Create anchor nodes from anchors
                nextWaypoint = AnchorNode(nextAnchor)

                // Set anchor node parent to the AR Scene
                nextWaypoint!!.parent = arSceneView.scene

                // Build a distance node with that distance and set parent to currentWaypoint
                buildDistanceNode(path[pathIndex].distanceTo(destination.getCentralLocation()))
                distanceNode.parent = currentWaypoint

            }
        }

        updateDistanceNode()

    }

    //endregion




}