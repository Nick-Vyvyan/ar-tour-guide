package com.example.artourguideapp

import android.location.Location
import com.example.artourguideapp.entities.Entity
import com.google.ar.core.Earth
import com.google.ar.core.GeospatialPose
import com.google.ar.core.TrackingState
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.ArSceneView
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3

/**
 * This class contains the functions used to create, update, and remove anchors in
 * the AR Scene. It also contains constants for proximities and function intervals.
 */
class AnchorHelper {

    companion object {
        const val ANCHOR_PROXIMITY_DISTANCE = 500f
        const val VISIBLE_NODE_PROXIMITY_DISTANCE = 150f

        const val ANCHOR_SET_INTERVAL_MS : Long = 30000
        const val UPDATE_NODE_INTERVAL_MS : Long = 250

        const val SCALE_MULTIPLIER = 0.5f
        const val SCALE_MIN_DISTANCE = 20

        fun scheduledSetAnchors(arSceneView: ArSceneView, entities: MutableList<Entity>) {

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
                    //      Create anchor, set node, and update scale and rotation
                    else if (!entity.nodeIsAttached()) {
                        createAnchorAndSetNode(earth, entity, arSceneView)
                        updateNodeScale(entity.getNode(), distance)
                        updateNodeRotation(entity.getNode(), arSceneView.scene.camera.worldPosition)
                    }
                }
            }
        }

        fun scheduledUpdateNodes(arSceneView: ArSceneView, entities: MutableList<Entity>) {
            for (entity in entities) {
                // If node is attached
                if (entity.nodeIsAttached()) {
                    // Get distance to Node
                    val distance = Vector3.subtract(arSceneView.scene.camera.worldPosition, entity.getNode().worldPosition).length()

                    // If node is within visible range, update scale and rotation
                    if (distance < VISIBLE_NODE_PROXIMITY_DISTANCE) {
                        updateNodeScale(entity.getNode(), distance)
                        updateNodeRotation(entity.getNode(), arSceneView.scene.camera.worldPosition)
                    }
                }
            }
        }

        private fun createAnchorAndSetNode(earth: Earth, entity: Entity, arSceneView: ArSceneView) {
            // Create anchor
            val entityAnchor = earth.createAnchor(
                entity.getCentralLocation().latitude,
                entity.getCentralLocation().longitude,
                earth.cameraGeospatialPose.altitude + 0.5f,
                0f, 0f, 0f, 1f
            )

            // Create anchor node from anchor
            val arAnchorNode = AnchorNode(entityAnchor)

            // Set entity node parent to anchor node
            entity.getNode().parent = arAnchorNode

            // Set anchor node parent to the AR Scene
            arAnchorNode.parent = arSceneView.scene
        }

        private fun updateNodeRotation(node: Node, userPosition: Vector3) {
            // Direction from user to entity
            val direction = Vector3.subtract(userPosition, node.worldPosition)

            // Updated rotation as Quaternion
            val lookRotation = Quaternion.lookRotation(direction, Vector3.up())

            node.worldRotation = lookRotation
        }

        private fun updateNodeScale(node: Node, distance: Float) {
            if (distance < SCALE_MIN_DISTANCE) {
                node.worldScale = Vector3(distance * SCALE_MULTIPLIER, distance * SCALE_MULTIPLIER, distance * SCALE_MULTIPLIER)
            } else
                node.worldScale = Vector3(25f, 25f, 25f)
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