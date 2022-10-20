package com.example.artourguideapp

import android.content.Context
import android.location.Location
import kotlin.math.abs

private const val PROXIMITY_RADIUS = 200

class User(
    context: Context
) {
    private var orientation = Orientation(context)
    private var userLocation = UserLocation(context)
    private lateinit var entitiesInView : List<Entity>

    /** TEMP FOR TESTING*/
    private var entityList : ArrayList<Location> = ArrayList()
    init {
        // Communications Facility
        var cfLoc : Location = Location("Communications Facility")
        cfLoc.latitude = 48.7327738818
        cfLoc.longitude = -122.485214413

        // Wade King Recreational Center
        var wkrcLoc : Location = Location("Wade King Recreational Center")
        wkrcLoc.latitude = 48.7315959997
        wkrcLoc.longitude = -122.488958036

        entityList.add(cfLoc)
        entityList.add(wkrcLoc)
    }

    fun getOrientation(): Orientation {
        return orientation
    }

    fun getLocation() : Location {
        return userLocation.getLocation()
    }

    fun getInView() : ArrayList<Location> {
        val nearbyEntities = detectNearbyEntities()
        var lookingAt : ArrayList<Location> = ArrayList()
        val myLocation = getLocation()
        val myHeading = Math.toDegrees(getOrientation().getOrientation()[0].toDouble())

        nearbyEntities.forEach {
            val bearingTo = myLocation.bearingTo(it)
            println("DEBUG - BEARING TO " + it.provider + " = " + bearingTo.toString())
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

    fun detectNearbyEntities() : ArrayList<Location> {
        var nearbyEntities : ArrayList<Location> = ArrayList()
        var myLocation = getLocation()
        // Loop through entity locations and detect which ones are within PROXIMITY_RADIUS
        entityList.forEach {
            if (myLocation.distanceTo(it) <= PROXIMITY_RADIUS) {
                println("Within " + PROXIMITY_RADIUS + " meters of " + it.provider)
                nearbyEntities.add(it)
            }
        }
        return nearbyEntities
    }
}