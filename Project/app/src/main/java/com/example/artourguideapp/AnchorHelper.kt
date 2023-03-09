package com.example.artourguideapp

import android.location.Location
import android.util.Log
import com.example.artourguideapp.entities.Entity
import com.google.android.gms.maps.model.LatLng
import com.google.ar.core.Earth
import com.google.ar.core.TrackingState
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.ArSceneView

/**
 * This class contains the functions used to create, update, and remove anchors in
 * the AR Scene.
 */
class AnchorHelper {
    companion object {
        var initialAnchorsPlaced = false

        
        /**
         * wrapper function used to set/update/remove anchors in an [ArSceneView] for a list of given [Entity] objects
         *
         * only updates anchors if device position accuracy is within a certain threshold
         *
         * @param arSceneView the sceneView that the anchors should be placed in
         * @param entities a [MutableList] of [Entity] objects that each need an anchor
        * */
        fun setAnchors(arSceneView: ArSceneView, entities: MutableList<Entity>) {
            // Get AR Earth
            val earth = arSceneView.session?.earth
            if (earth?.trackingState == TrackingState.TRACKING) {

                val horizontalAccuracy = earth.cameraGeospatialPose.horizontalAccuracy
                val verticalAccuracy = earth.cameraGeospatialPose.verticalAccuracy

                // check device position accuracy against a given threshold
                // prevents (hopefully) inaccurate anchor placement
                if (horizontalAccuracy > AppSettings.ACCURACY_MAX_THRESHOLD || verticalAccuracy > AppSettings.ACCURACY_MAX_THRESHOLD) {
                    Log.d("ANCHOR HELPER", "Anchors not placed - low accuracy")
                    return
                }

                // Get user location - used to determine proximity
                val userLatLng = LatLng(earth.cameraGeospatialPose.latitude, earth.cameraGeospatialPose.longitude)

                // Attempt to set anchor for all entities
                for (entity in entities) {
                    val entityLatLng = entity.getLatLng()

                    var results = FloatArray(1)
                    // Get distance to entity
                    Location.distanceBetween(userLatLng.latitude, userLatLng.longitude,
                                             entityLatLng.latitude, entityLatLng.longitude,
                                             results)

                    val distance = results[0]

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

        /**
         *
         * function is used to set an individual [AnchorNode] for a specific [Entity]
         *
         * @param earth the [Earth] that is currently being tracked by the [ArSceneView]
         * @param entity the specific [Entity] that the anchor is for
         * @param arSceneView the [ArSceneView] that the anchor is placed in
         * */
        private fun createAnchorAndSetNode(earth: Earth, entity: Entity, arSceneView: ArSceneView) {
            // Create anchor
            val entityAnchor = earth.createAnchor(
                entity.getLatLng().latitude,
                entity.getLatLng().longitude,
                earth.cameraGeospatialPose.altitude,
                0f, 0f, 0f, 1f
            )

            // Create anchor node from anchor
            val arAnchorNode = AnchorNode(entityAnchor)

            // Set entity node parent to anchor node
            entity.getNode()?.parent = arAnchorNode

            // Set anchor node parent to the AR Scene
            arAnchorNode.parent = arSceneView.scene
        }

        /**
         * detaches an Anchor from ARCore Session and sets parents to null
         *
         * @param entity the [Entity] that should have its anchor removed
         * */
        private fun removeAnchor(entity: Entity) {
            if (entity.nodeIsAttached()) {
                val anchorNode = entity.getNode()!!.parent as AnchorNode
                anchorNode.anchor?.detach()
                anchorNode.parent = null
                entity.getNode()!!.parent = null
            }
        }
    }



}