package com.example.artourguideapp

import android.location.Location
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
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
        private lateinit var destination: Entity
        private lateinit var navButton: Button // Button on ArActivity

        /**
         * Initializes the navigation object and navButton
         */
        fun init(arSceneView: ArSceneView, activity: AppCompatActivity, navButton: Button) {
            navigation = Navigation(arSceneView, activity)
            this.navButton = navButton
        }

        /**
         * Starts navigation to a destination
         */
        fun startNavigationTo(destination: Entity) {
            if (navigationInitialized()) {
                navigation.setDestination(destination)
                navigation.startNavigation()
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

    private val ARROW_UPDATE_INTERVAL: Long = 2000
    private val ARROW_PLACEMENT_DISTANCE_MULTIPLIER = .00001
    private val ARROW_HEIGHT_DISPLACEMENT = -0.3f

    // Once object is constructed, start building the AR arrow
    init {
        buildArrow()
    }

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

    // Set entity as destination
    private fun setDestination(newDestination: Entity) {
        newDestination.setAsDestination()
        destination = newDestination
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
        if (shouldPlaceNewArrow()) {
            activity.runOnUiThread {
                placeNewArrow()
            }
        }
        else {
            setArrowRotation()
        }
    }

    // Stop navigation. Cancel nav updates. Clear destination. Detach anchors, set arrowNode parent to null
    private fun stopNavigation() {
        arrowUpdateTimer.cancel()
        destination.clearAsDestination()
        arrowNode.parent = null
        arrowAnchor?.detach()
        arrowPlaced = false
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


}