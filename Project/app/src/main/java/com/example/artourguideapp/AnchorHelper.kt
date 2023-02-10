package com.example.artourguideapp

import android.location.Location
import android.os.Debug
import android.util.Log
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
        var initialAnchorsPlaced = false

        fun setAnchors(arSceneView: ArSceneView, entities: MutableList<Entity>) {
            // Get AR Earth
            val earth = arSceneView.session?.earth
            if (earth?.trackingState == TrackingState.TRACKING) {

                val horizontalAccuracy = earth.cameraGeospatialPose.horizontalAccuracy
                val verticalAccuracy = earth.cameraGeospatialPose.verticalAccuracy

                if (horizontalAccuracy > AppSettings.ACCURACY_MAX_THRESHOLD || verticalAccuracy > AppSettings.ACCURACY_MAX_THRESHOLD) {
                    Log.d("ANCHOR HELPER", "Anchors not placed - low accuracy")
                    return
                }

                // Get user location
                val userLocation = Location("User")
                userLocation.latitude = earth.cameraGeospatialPose.latitude
                userLocation.longitude = earth.cameraGeospatialPose.longitude

                // Attempt to set anchor for all entities
                for (entity in entities) {

                    // Get distance to entity
                    val distance = userLocation.distanceTo(entity.getCentralLocation())

                    // If entity is too far away or is not the destination, remove the anchor
                    if (distance > AppSettings.ANCHOR_PROXIMITY_DISTANCE && !entity.isDestination()) {
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
                earth.cameraGeospatialPose.altitude,
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