package com.example.artourguideapp.navigation

import android.location.Location

class DummyWaypoints {

    companion object {

        private var graph = WaypointsGraph()

        private var dummyLocation1 = Location("Dummy 1")
        private var dummyLocation2 = Location("Dummy 2")
        private var dummyLocation3 = Location("Dummy 3")
        private var dummyLocation4 = Location("Dummy 4")
        private var dummyLocation5 = Location("Dummy 5")
        private var dummyLocation6 = Location("Dummy 6")

        fun getDummyGraph(): WaypointsGraph {
            if (graph.size() <= 0) {
                initializeLocations()
                constructDummyGraph()
            }

            return graph
        }

        private fun initializeLocations() {
            dummyLocation1.latitude = 48.76240740727621
            dummyLocation1.longitude = -122.45073105058073

            dummyLocation2.latitude = 48.762443151417344
            dummyLocation2.longitude = -122.45074221453696

            dummyLocation3.latitude = 48.76242738194645
            dummyLocation3.longitude = -122.45061303161467

            dummyLocation4.latitude = 48.76265551314312
            dummyLocation4.longitude = -122.45055721183343

            dummyLocation5.latitude = 48.76262712821195
            dummyLocation5.longitude = -122.45033552755933

            dummyLocation6.latitude = 48.762424228051685
            dummyLocation6.longitude = -122.45031798419951
        }

        private fun constructDummyGraph() {
            val vertex1 = graph.createVertex(dummyLocation1)
            val vertex2 = graph.createVertex(dummyLocation2)
            val vertex3 = graph.createVertex(dummyLocation3)
            val vertex4 = graph.createVertex(dummyLocation4)
            val vertex5 = graph.createVertex(dummyLocation5)
            val vertex6 = graph.createVertex(dummyLocation6)

            graph.addEdge(vertex1, vertex2)
            graph.addEdge(vertex2, vertex3)
            graph.addEdge(vertex3, vertex4)
            graph.addEdge(vertex4, vertex5)
            graph.addEdge(vertex5, vertex6)
            graph.addEdge(vertex6, vertex3)
        }


    }

}