package com.example.artourguideapp.arcore

import android.location.Location
import com.example.artourguideapp.entities.Entity
import com.google.android.gms.maps.model.LatLng
import com.google.ar.core.Anchor
import com.google.ar.core.Earth
import com.google.ar.core.Session
import com.google.ar.core.TrackingState

class AnchorHelper {
    companion object {

        /** detach and remove anchors that are not within 200 meters
         * of the current device location */
        fun removeUnusedAnchors(
            entityList: MutableList<Entity>,
            userLocation : Location
        ) {
            for (currentEntity in entityList) {
                if (userLocation.distanceTo(currentEntity.getCentralLocation()) > 200) {
                    val anchor = currentEntity.getAnchor()
                    // TODO: I don't really know if this works (max anchors is 100?)
                    anchor?.detach()
                }
            }
        }

        /** returns a list of anchors that are within
         * 200 meters of the current position. Also creates anchors
         * for entities that do not already have one */
        fun updateCurrentAnchors(
            earth: Earth,
            entityList: MutableList<Entity>,
            userLocation: Location
        ) : MutableList<Anchor> {
            val anchorList = mutableListOf<Anchor>()

            for (currentEntity in entityList) {
                val currLocation = currentEntity.getCentralLocation()

                if (userLocation.distanceTo(currLocation) < 200) {
                    val currAnchor = currentEntity.getAnchor()

                    if (currAnchor != null) {
                        anchorList.add(currAnchor)
                    } else {
                        val newAnchor = earth.resolveAnchorOnTerrain(
                            currLocation.latitude,
                            currLocation.longitude,
                            5.0,
                            0f,0f,0f,1f
                        )
                        currentEntity.setAnchor(newAnchor)
                        anchorList.add(currentEntity.getAnchor()!!)
                    }
                }
            }
            return anchorList
        }

    }
}