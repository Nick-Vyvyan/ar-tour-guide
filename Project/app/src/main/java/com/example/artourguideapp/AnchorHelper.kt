package com.example.artourguideapp

import android.location.Location
import com.example.artourguideapp.entities.Entity
import com.google.ar.core.Earth
import com.google.ar.core.TrackingState
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.ArSceneView

class AnchorHelper {

    /**
     * This class contains the functions used to create, update, and remove anchors in
     * the AR Scene. It also contains constants for proximities and function intervals.
     */
    companion object {
        const val ANCHOR_PROXIMITY_DISTANCE = 500f
        const val ANCHOR_ELEVATION_OFFSET = 1.5f
        const val INITIAL_ANCHOR_SET_INTERVAL_MS : Long = 100
        const val ANCHOR_SET_INTERVAL_MS : Long = 10000

        const val VISIBLE_NODE_PROXIMITY_DISTANCE = 150f

        const val SCALE_MULTIPLIER = 0.5f
        const val SCALE_MAX_DISTANCE = 50
        const val DEFAULT_SCALE = SCALE_MAX_DISTANCE * SCALE_MULTIPLIER

        var initialAnchorsPlaced = false

        fun setAnchors(arSceneView: ArSceneView, entities: MutableList<Entity>) {

            // Get AR Earth
            val earth = arSceneView.session?.earth
            if (earth?.trackingState == TrackingState.TRACKING) {
                // Get user location
                val userLocation = Location("User")
                userLocation.latitude = earth.cameraGeospatialPose.latitude
                userLocation.longitude = earth.cameraGeospatialPose.longitude

                // Attempt to set anchor for all entities
                for (entity in entities) {

                    // Get distance to entity
                    val distance = userLocation.distanceTo(entity.getCentralLocation())

                    // If entity is too far away or is not the destination, remove the anchor
                    if (distance > ANCHOR_PROXIMITY_DISTANCE && !entity.isDestination()) {
                        removeAnchor(entity)
                    }
                    // If entity in proximity or is destination, but node is not yet attached
                    //      Create anchor and set node
                    else if (!entity.nodeIsAttached()) {
                        createAnchorAndSetNode(earth, entity, arSceneView)
                    }
                }
                initialAnchorsPlaced = true
            }
        }

        private fun createAnchorAndSetNode(earth: Earth, entity: Entity, arSceneView: ArSceneView) {
            // Create anchor
            val entityAnchor = earth.createAnchor(
                entity.getCentralLocation().latitude,
                entity.getCentralLocation().longitude,
                earth.cameraGeospatialPose.altitude + ANCHOR_ELEVATION_OFFSET,
                0f, 0f, 0f, 1f
            )

            // Create anchor node from anchor
            val arAnchorNode = AnchorNode(entityAnchor)

            // Set entity node parent to anchor node
            entity.getNode().parent = arAnchorNode

            // Set anchor node parent to the AR Scene
            arAnchorNode.parent = arSceneView.scene
        }

        private fun removeAnchor(entity: Entity) {
            if (entity.nodeIsAttached()) {
                val anchorNode = entity.getNode().parent as AnchorNode
                anchorNode.anchor?.detach()
                anchorNode.parent = null
                entity.getNode().parent = null
            }
        }
    }



}