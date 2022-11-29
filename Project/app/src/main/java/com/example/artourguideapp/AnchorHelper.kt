package com.example.artourguideapp

import android.location.Location
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.artourguideapp.entities.DummyBuildingEntities
import com.example.artourguideapp.entities.Entity
import com.google.android.gms.maps.model.LatLng
import com.google.ar.core.Anchor
import com.google.ar.core.Earth
import com.google.ar.core.GeospatialPose
import com.google.ar.core.Session
import com.google.ar.core.TrackingState
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.ArSceneView
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class AnchorHelper {

    companion object {
        const val PROXIMITY_DISTANCE = 150
        const val DISTANCE_MULTIPLIER = 0.5f
        const val LOCATION_ACCURACY_LIMIT = 30

        fun attemptSetAnchor(entity: Entity, arSceneView: ArSceneView) {
            val earth = arSceneView.session?.earth
            if (earth?.trackingState == TrackingState.TRACKING) {
                val userLocation = Location("user")
                userLocation.latitude = earth.cameraGeospatialPose.latitude
                userLocation.longitude = earth.cameraGeospatialPose.longitude

                val distance = userLocation.distanceTo(entity.getCentralLocation())
                val direction = Vector3.subtract(arSceneView.scene.camera.worldPosition, entity.getNode().worldPosition)
//                Log.d("AnchorHelper", "DISTANCE TO ${entity.getName()} = $distance")

                if (entityInProximity(distance) && !entity.nodeIsAttached()) {
                    val entityAnchor = earth.createAnchor(
                        entity.getCentralLocation().latitude,
                        entity.getCentralLocation().longitude,
                        earth.cameraGeospatialPose.altitude + 0.5f,
                        0f, 0f, 0f, 1f
                    )

                    val arAnchorNode = AnchorNode(entityAnchor)
                    entity.getNode().parent = arAnchorNode

                    updateNodeScale(entity.getNode(), distance)
                    updateNodeRotation(entity.getNode(), earth.cameraGeospatialPose, direction)

                    arAnchorNode.parent = arSceneView.scene
//                    Log.d("AnchorHelper", "Node for ${entity.getName()} has been ATTACHED")
                } else if (entityInProximity(distance) && entity.nodeIsAttached() && entity.getNode().isActive && entity.getNode().isEnabled) {
//                    Log.d("AnchorHelper", "NODE LOCATION FOR ${entity.getName()} = ${entity.getNode().worldPosition}")
                    updateNodeScale(entity.getNode(), distance)
                    updateNodeRotation(entity.getNode(), earth.cameraGeospatialPose, direction)
                } else {
                    removeAnchor(entity)
//                    Log.d("AnchorHelper", "Node for ${entity.getName()} is DETACHED")
                }
            }
        }

        private fun updateNodeScale(node: Node, distance: Float) {
            if (distance < 20) {
                node.worldScale = Vector3(distance * DISTANCE_MULTIPLIER, distance * DISTANCE_MULTIPLIER, distance * DISTANCE_MULTIPLIER)
            } else
                node.worldScale = Vector3(25f, 25f, 25f)

//            Log.d("AnchorHelper", "Node Scale Updated for ${node.name}")
        }

        private fun updateNodeRotation(node: Node, pose: GeospatialPose, direction: Vector3) {
//            Log.d("AnchorHelper", "Inside updateNodeRotation - Location Accuracy = ${pose.horizontalAccuracy}")
            if (pose.horizontalAccuracy < LOCATION_ACCURACY_LIMIT) {
                val lookRotation = Quaternion.lookRotation(direction, Vector3.up())
                node.worldRotation = lookRotation
//                Log.d("AnchorHelper", "Updated rotation for ${node.name}")
            }
        }

        private fun removeAnchor(entity: Entity) {
            if (entity.getNode().parent != null) {
                val anchorNode = entity.getNode().parent as AnchorNode
                anchorNode.anchor?.detach()
                anchorNode.parent = null
                entity.getNode().parent = null
            }
        }

        private fun entityInProximity(distance : Float) : Boolean {
            return distance < PROXIMITY_DISTANCE
        }

    }



}