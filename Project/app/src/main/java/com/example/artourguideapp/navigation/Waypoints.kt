package com.example.artourguideapp.navigation

import android.location.Location

class Waypoints() {

    private val USE_DUMMY_LOCATIONS = true

    private var waypointsGraph: WaypointsGraph = if (USE_DUMMY_LOCATIONS) DummyWaypoints.getDummyGraph()
                                                 else CampusWaypoints.getCampusGraph()

    fun getPathFromTo(source: Location, destination: Location): MutableList<Location> {
        return waypointsGraph.shortestPath(waypointsGraph.getClosestVertexToLocation(source), waypointsGraph.getClosestVertexToLocation(destination))
    }
}
