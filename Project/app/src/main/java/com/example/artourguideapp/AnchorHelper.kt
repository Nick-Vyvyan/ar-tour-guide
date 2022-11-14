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
        val PROXIMTY_DISTANCE = 150
        val DISTANCE_MULTIPLIER = 0.5f

        fun attemptSetAnchor(entity: Entity, arSceneView: ArSceneView) {
            val earth = arSceneView.session?.earth
            if (earth?.trackingState == TrackingState.TRACKING) {
                val userLocation = Location("user")
                userLocation.latitude = earth.cameraGeospatialPose.latitude
                userLocation.longitude = earth.cameraGeospatialPose.longitude

                val distance = userLocation.distanceTo(entity.getCentralLocation())
                val angle =  userLocation.bearingTo(entity.getCentralLocation())
                println("DISTANCE TO ${entity.getName()} = $distance")

                if (entityInProximity(distance) && !entity.nodeIsAttached()) {
                    val entityAnchor = earth.createAnchor(
                        entity.getCentralLocation().latitude,
                        entity.getCentralLocation().longitude,
                        earth.cameraGeospatialPose.altitude + 0.5f,
                        0f,0f,0f,1f
                    )

                    val arAnchorNode = AnchorNode(entityAnchor)
                    entity.getNode().parent = arAnchorNode

                    updateNodeScale(entity.getNode(), distance)
                    updateNodeRotation(entity.getNode(), earth.cameraGeospatialPose)

                    arAnchorNode.parent = arSceneView.scene
                    Log.d("AnchorHelper", "Node for ${entity.getName()} has been ATTACHED")
                } else if (entityInProximity(distance) && entity.nodeIsAttached()) {
                    // TODO: update anchor rotation
                    updateNodeScale(entity.getNode(), distance)
                    updateNodeRotation(entity.getNode(), earth.cameraGeospatialPose)
                    Log.d("AnchorHelper", "Node for ${entity.getName()} has been UPDATED")
                } else {
                    removeAnchor(entity)
                    Log.d("AnchorHelper", "Node for ${entity.getName()} has been DETACHED")
                }
            }
        }

        private fun updateNodeScale(node: Node, distance: Float) {
            if (distance < 35) {
                node.localScale = Vector3(distance * DISTANCE_MULTIPLIER, distance * DISTANCE_MULTIPLIER, distance * DISTANCE_MULTIPLIER)
            } else
                node.localScale = Vector3(100f, 100f, 100f)

            Log.d("AnchorHelper", "Node Scale Updated")
        }

        private fun updateNodeRotation(node: Node, pose: GeospatialPose) {
            node.worldRotation = Quaternion(
                0f,
                sin((PI - Math.toRadians(pose.heading)) / 2).toFloat(),
                0f,
                cos((PI - Math.toRadians(pose.heading)) / 2).toFloat()
            )
            node.setLookDirection(Vector3(0f, 0f, pose.heading.toFloat()))
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
            return distance < PROXIMTY_DISTANCE
        }

    }



}