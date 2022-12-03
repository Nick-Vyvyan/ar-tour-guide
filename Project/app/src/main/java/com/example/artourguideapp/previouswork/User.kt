package com.example.artourguideapp.previouswork

import android.content.Context
import android.location.Location
import com.example.artourguideapp.entities.*
import kotlin.math.abs

private const val PROXIMITY_RADIUS = 200

class User(
    context: Context
) {
    private var orientation = Orientation(context)
    private var userLocation = UserLocation(context)
    private lateinit var entitiesInView : List<Entity>

    /** TEMP FOR TESTING*/
    private var entityList : ArrayList<Entity> = ArrayList()

    init {
        entityList.add(DummyBuildingEntities.commFacilityEntity)
        entityList.add(DummyBuildingEntities.wadeKingEntity)
    }

    fun getOrientation(): Orientation {
        return orientation
    }

    fun getLocation() : Location {
        return userLocation.getLocation()
    }

    fun getInView() : ArrayList<Entity> {
        val nearbyEntities = detectNearbyEntities()
        var lookingAt : ArrayList<Entity> = ArrayList()
        val myLocation = getLocation()
        val myHeading = Math.toDegrees(getOrientation().getOrientation()[0].toDouble())

        nearbyEntities.forEach {
            val bearingTo = myLocation.bearingTo(it.getCentralLocation())
            println("DEBUG - BEARING TO " + it.getName() + " = " + bearingTo.toString())
            println("DEBUG - MY HEADING = $myHeading")

            var headingDiff = bearingTo - myHeading
            if (headingDiff > 180) {
                headingDiff += -360
            } else if (headingDiff < -180) {
                headingDiff += 360
            } else {
                // nothing
            }
            println("DEBUG - DIFFERENCE IN HEADINGS = $headingDiff")
            if (abs(headingDiff) < 45) {
                lookingAt.add(it)
            }
        }

        return lookingAt
    }

    fun hasViewedAllNearbyBuildings() {
        TODO("Not yet implemented")
    }

    fun detectNearbyEntities() : ArrayList<Entity> {
        var nearbyEntities : ArrayList<Entity> = ArrayList()
        var myLocation = getLocation()
        // Loop through entity locations and detect which ones are within PROXIMITY_RADIUS
        entityList.forEach {
            if (myLocation.distanceTo(it.getCentralLocation()) <= PROXIMITY_RADIUS) {
                println("Within " + PROXIMITY_RADIUS + " meters of " + it.getName())
                nearbyEntities.add(it)
            }
        }
        return nearbyEntities
    }
}