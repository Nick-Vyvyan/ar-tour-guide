package com.example.artourguideapp

import android.location.Location
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

class AnchorHelper {

    companion object {
        val PROXIMTY_DISTANCE = 125
        val DISTANCE_MULTIPLIER = 2

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
                        earth.cameraGeospatialPose.altitude + 1,
                        0f,earth.cameraGeospatialPose.heading.toFloat(),0f,1f
                    )

                    val arAnchorNode = AnchorNode(entityAnchor)
                    entity.getNode().parent = arAnchorNode

                    updateNodeScale(entity.getNode(), distance)
                    updateNodeRotation(entity.getNode(), angle)

                    arAnchorNode.parent = arSceneView.scene
                    println("DEBUG - ANCHOR NODE CREATED AND SET FOR ${entity.getName()}")
                } else if (entityInProximity(distance) && entity.nodeIsAttached()) {
                    // TODO: update anchor rotation
                    updateNodeScale(entity.getNode(), distance)
                    updateNodeRotation(entity.getNode(), angle)
                } else {
                    removeAnchor(entity)
                }
            }
        }

        private fun updateNodeScale(node: Node, distance: Float) {
            node.localScale = Vector3(distance * DISTANCE_MULTIPLIER, distance * DISTANCE_MULTIPLIER, distance * DISTANCE_MULTIPLIER)
        }

        private fun updateNodeRotation(node: Node, angle: Float) {
            node.worldRotation = Quaternion(Vector3(0f, angle, 0f))
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