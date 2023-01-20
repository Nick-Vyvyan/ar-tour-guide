package com.example.artourguideapp.navigation

import android.location.Location

class PathFactory() {

    companion object {
        private val USE_DUMMY_LOCATIONS = true

        private var waypointsGraph: WaypointsGraph = if (USE_DUMMY_LOCATIONS) DummyWaypoints.getDummyGraph()
                                                     else CampusWaypoints.getCampusGraph()

        fun getPathFromTo(source: Location, destination: Location): NavigationPath {
            var path = waypointsGraph.shortestPath(source, destination)
            path.waypoints.add(destination)

            return path
        }
    }
}
