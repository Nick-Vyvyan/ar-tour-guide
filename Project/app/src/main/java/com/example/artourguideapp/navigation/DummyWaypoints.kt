package com.example.artourguideapp.navigation

import android.location.Location

class DummyWaypoints {

    companion object {

        private var USE_DUMMY_CAMPUS_LOCATIONS = true

        private var graph = WaypointsGraph()

        private var dummyLocation1 = Location("Dummy 1")
        private var dummyLocation2 = Location("Dummy 2")
        private var dummyLocation3 = Location("Dummy 3")
        private var dummyLocation4 = Location("Dummy 4")
        private var dummyLocation5 = Location("Dummy 5")
        private var dummyLocation6 = Location("Dummy 6")

        fun getDummyGraph(): WaypointsGraph {
            if (graph.size() <= 0) {
                if (USE_DUMMY_CAMPUS_LOCATIONS) {
                    initializeDummySouthCampusLocations()
                    constructDummySouthCampusGraph()
                }
                else {
                    initializeLocations()
                    constructDummyGraph()
                }
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

        //region South Campus

        //region Dummy South Campus Locations

        private var south1 = Location("South 1")
        private var south2 = Location("South 2")
        private var south3 = Location("South 3")
        private var south4 = Location("South 4")
        private var south5 = Location("South 5")
        private var south6 = Location("South 6")
        private var south7 = Location("South 7")
        private var south8 = Location("South 8")
        private var south9 = Location("South 9")
        private var south10 = Location("South 10")
        private var south11 = Location("South 11")
        private var south12 = Location("South 12")
        private var south13 = Location("South 13")
        private var south14 = Location("South 14")
        private var south15 = Location("South 15")
        private var south16 = Location("South 16")
        private var south17 = Location("South 17")
        private var south18 = Location("South 18")
        private var south19 = Location("South 19")
        private var south20 = Location("South 20")
        private var south21 = Location("South 21")

        //endregion

        private fun initializeDummySouthCampusLocations() {
            south1.latitude = 48.731078993947094
            south1.longitude = -122.48941993021289

            south2.latitude = 48.731078993947094
            south2.longitude = -122.48844106067601

            south3.latitude = 48.73106114168844
            south3.longitude = -122.48723213424337

            south4.latitude = 48.73114742754665
            south4.longitude = -122.48712838309427

            south5.latitude = 48.73127753031427
            south5.longitude = -122.48719286776725

            south6.latitude = 48.73136509929524
            south6.longitude = -122.48743942681101

            south7.latitude = 48.7314351543702
            south7.longitude = -122.48763288082999

            south8.latitude = 48.73156025247557
            south8.longitude = -122.48809565318902

            south9.latitude = 48.731918031338864
            south9.longitude = -122.48782633484892

            south10.latitude = 48.731725381498066
            south10.longitude = -122.48715114239063

            south11.latitude = 48.73245594582983
            south11.longitude = -122.48744321999659

            south12.latitude = 48.73226329805007
            south12.longitude = -122.48679837326672

            south13.latitude = 48.7320831591986
            south13.longitude = -122.48630146196315

            south14.latitude = 48.73253350512003
            south14.longitude = -122.48663905819785

            south15.latitude = 48.732388394097875
            south15.longitude = -122.48629387553659

            south16.latitude = 48.73246845540328
            south16.longitude = -122.48580834388116

            south17.latitude = 48.73269112523854
            south17.longitude = -122.48554281875711

            south18.latitude = 48.733081420548146
            south18.longitude = -122.4854897137323

            south19.latitude = 48.73321652206481
            south19.longitude = -122.4856528220228

            south20.latitude = 48.73328657456014
            south20.longitude = -122.48608145543733

            south21.latitude = 48.73288377137901
            south21.longitude = -122.48638870593804
        }

        private fun constructDummySouthCampusGraph() {
            val vertex1 = graph.createVertex(south1)
            val vertex2 = graph.createVertex(south2)
            val vertex3 = graph.createVertex(south3)
            val vertex4 = graph.createVertex(south4)
            val vertex5 = graph.createVertex(south5)
            val vertex6 = graph.createVertex(south6)
            val vertex7 = graph.createVertex(south7)
            val vertex8 = graph.createVertex(south8)
            val vertex9 = graph.createVertex(south9)
            val vertex10 = graph.createVertex(south10)
            val vertex11 = graph.createVertex(south11)
            val vertex12 = graph.createVertex(south12)
            val vertex13 = graph.createVertex(south13)
            val vertex14 = graph.createVertex(south14)
            val vertex15 = graph.createVertex(south15)
            val vertex16 = graph.createVertex(south16)
            val vertex17 = graph.createVertex(south17)
            val vertex18 = graph.createVertex(south18)
            val vertex19 = graph.createVertex(south19)
            val vertex20 = graph.createVertex(south20)
            val vertex21 = graph.createVertex(south21)

            graph.addEdge(vertex1, vertex2)

            graph.addEdge(vertex2, vertex3)
            graph.addEdge(vertex2, vertex7)
            graph.addEdge(vertex2, vertex8)

            graph.addEdge(vertex3, vertex4)

            graph.addEdge(vertex4, vertex5)

            graph.addEdge(vertex5, vertex6)

            graph.addEdge(vertex6, vertex7)
            graph.addEdge(vertex6, vertex10)

            graph.addEdge(vertex7, vertex8)

            graph.addEdge(vertex8, vertex9)

            graph.addEdge(vertex9, vertex10)
            graph.addEdge(vertex9, vertex11)

            graph.addEdge(vertex10, vertex12)

            graph.addEdge(vertex11, vertex12)

            graph.addEdge(vertex12, vertex14)

            graph.addEdge(vertex13, vertex15)

            graph.addEdge(vertex14, vertex15)
            graph.addEdge(vertex14, vertex21)

            graph.addEdge(vertex15, vertex16)

            graph.addEdge(vertex16, vertex17)

            graph.addEdge(vertex17, vertex18)

            graph.addEdge(vertex18, vertex19)

            graph.addEdge(vertex19, vertex20)

            graph.addEdge(vertex20, vertex21)
        }

        //endregion
    }

}