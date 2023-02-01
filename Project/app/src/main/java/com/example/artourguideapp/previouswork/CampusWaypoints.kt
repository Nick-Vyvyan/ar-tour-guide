package com.example.artourguideapp.previouswork

class CampusWaypoints {
    companion object {
        private var graph = WaypointsGraph()

        fun getCampusGraph(): WaypointsGraph {
            if (graph.size() <= 0) {
                initializeLocations()
                constructGraph()
            }

            return graph
        }

        private fun initializeLocations() {

        }

        private fun constructGraph() {

        }

    }
}