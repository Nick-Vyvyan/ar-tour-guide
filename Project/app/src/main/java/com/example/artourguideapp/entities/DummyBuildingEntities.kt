package com.example.artourguideapp.entities

import android.location.Location
import androidx.appcompat.app.AppCompatActivity

/**
 * This class serves as a holder for Building Entities until we get data loaded in
 * from the json file
 */
class DummyBuildingEntities : java.io.Serializable {
    companion object {
        var cfLoc : Location = Location("Communications Facility")
        var wkrcLoc : Location = Location("Wade King Recreational Center")
        var awLoc : Location = Location("Academic West")
        var esLoc = Location("Environmental Studies")

        var h1Loc : Location = Location("House 1")
        var h2Loc : Location = Location("House 2")
        var h3Loc : Location = Location("House 3")
        var h4Loc : Location = Location("House 4")
        var h5Loc : Location = Location("House 5")
        var stairLoc : Location = Location("Stairs")

        var entityList = ArrayList<Entity>()

        var commFacilityEntity = BuildingEntity(
            "Communications Facility",
            ArrayList<Location>(),
            cfLoc,
            BuildingData(
            "Communications Facility",
            "CF",
            "Academic",
            "Communication Studies\nComputer Science\nJournalism\nPhysics and Astronomy",
            "Button activated entrances are located on the east and west sides of the building" +
                    "\nThere are accessible restrooms located on all floors" +
                    "\nCentrally located elevators provide access to all floors" +
                    "\nAccessible parking is available to the east (Lot 17 G)",
            "CF 157",
            "CF 21\n" +
                    "CF 24\n" +
                    "CF 26\n" +
                    "CF 161\n" +
                    "CF 165\n" +
                    "CF 167\n" +
                    "CF 312",
            "",
            "",
            "https://www.wwu.edu/building/cf")
        )

        var wadeKingEntity = BuildingEntity(
                "Wade King Recreational Center",
        ArrayList<Location>(),
        wkrcLoc,
                BuildingData(
        "Wade King Recreation Center",
        "SV",
        "Events\nRecreation",
        "Campus Recreation Services\n" +
        "Sport Clubs",
        "Accessible parking to the northeast (Lot 19G)\n" +
        "Accessible entrances on the east side\n" +
        "Accessibility notes:\n" +
        "\tAccessible rest rooms on the main floor",
        "SV 110, 155, 156",
        "",
        "Rock's Edge Cafe",
        "",
        "https://www.wwu.edu/building/sv")
        )

        var handel = LandmarkEntity("Mark di Suvero, For Handel, 1975", ArrayList<Location>(), Location("Mark di Suvero, For Handel, 1975"), LandmarkData(
            "Mark di Suvero, For Handel, 1975", "Di Suvero's knowledge of music and sensitivity to the relationship of art and architecture led him to create a soaring sculpture dedicated to the " +
                    "composer George Frederic Handel. Di Suvero's work rises not only from the roof of the rehearsal hall below but also projects beyond this roof/plaza and against a " +
                    "magnificent view of water, mountains and sky. Sometimes di Suvero is considered an \"action sculptor\" in the way he draws directly with the steel I- beams. In " +
                    "running his own truck cranes, in using his welding torch and in directing the blocks and cables, he attempts to build multi- dimensional structures which seem to " +
                    "overcome physical laws." +
                    "\n" +
                    "\n" +
                    "Di Suvero works with both the I-beams of modern buildings and the discarded materials of modern life. When he came to campus di Suvero found that a work he " +
                    "had been carrying around in his mind would fit the space of the newly reconstructed Music building and plaza. He has often stated that his sculptural ideas evolve " +
                    "in ''dreamtime ... pure music of the mind.\" He likes music, whether classical or jazz, because it is an example of disciplined emotion. He also can relate to the " +
                    "rigorous labor and challenges involved in the construction trade and engineering. Di Suvero's For Handel (1975) originally combined a hanging wooden platform or " +
                    "swinging bed with the steel girders of modern technology. The fact that the bed was removed soon after the sculpture was erected does not diminish the sculpture's " +
                    "impact or meaning.", "", "https://westerngallery.wwu.edu/mark-di-suvero-handel-1975"
        )
        )

        var stairsToNowhere = LandmarkEntity(
            "Stairs To Nowhere",
            ArrayList<Location>(),
            stairLoc,
            LandmarkData(
                "Stairs to Nowhere",
                "tellus cras adipiscing enim eu turpis egestas pretium aenean pharetra magna ac placerat vestibulum lectus",
                "tellus cras adipiscing enim eu turpis egestas pretium aenean pharetra magna ac placerat vestibulum lectus",
                "https://westerngallery.wwu.edu/mark-di-suvero-handel-1975"
            )
        )


        var house1 = BuildingEntity(
            "house1",
            ArrayList<Location>(),
            h1Loc,
            BuildingData(
                "house1",
                "H1",
                "Residential",
                "Communication Studies\nComputer Science\nJournalism\nPhysics and Astronomy",
                "Button activated entrances are located on the east and west sides of the building" +
                        "\nThere are accessible restrooms located on all floors" +
                        "\nCentrally located elevators provide access to all floors" +
                        "\nAccessible parking is available to the east (Lot 17 G)",
                "CF 157",
                "CF 21\n" +
                        "CF 24\n" +
                        "CF 26\n" +
                        "CF 161\n" +
                        "CF 165\n" +
                        "CF 167\n" +
                        "CF 312",
                "",
                "",
                "https://www.wwu.edu/building/cf")
        )

        var house2 = BuildingEntity(
            "house2",
            ArrayList<Location>(),
            h2Loc,
            BuildingData(
                "house2",
                "H2",
                "Residential",
                "Communication Studies\nComputer Science\nJournalism\nPhysics and Astronomy",
                "Button activated entrances are located on the east and west sides of the building" +
                        "\nThere are accessible restrooms located on all floors" +
                        "\nCentrally located elevators provide access to all floors" +
                        "\nAccessible parking is available to the east (Lot 17 G)",
                "CF 157",
                "CF 21\n" +
                        "CF 24\n" +
                        "CF 26\n" +
                        "CF 161\n" +
                        "CF 165\n" +
                        "CF 167\n" +
                        "CF 312",
                "",
                "",
                "https://www.wwu.edu/building/cf")
        )

        var house3 = BuildingEntity(
            "house3",
            ArrayList<Location>(),
            h3Loc,
            BuildingData(
                "3",
                "3",
                "Residential",
                "Communication Studies\nComputer Science\nJournalism\nPhysics and Astronomy",
                "Button activated entrances are located on the east and west sides of the building" +
                        "\nThere are accessible restrooms located on all floors" +
                        "\nCentrally located elevators provide access to all floors" +
                        "\nAccessible parking is available to the east (Lot 17 G)",
                "CF 157",
                "CF 21\n" +
                        "CF 24\n" +
                        "CF 26\n" +
                        "CF 161\n" +
                        "CF 165\n" +
                        "CF 167\n" +
                        "CF 312",
                "",
                "",
                "https://www.wwu.edu/building/cf")
        )

        var house4 = BuildingEntity(
            "house4",
            ArrayList<Location>(),
            h4Loc,
            BuildingData(
                "4",
                "4",
                "Residential",
                "Communication Studies\nComputer Science\nJournalism\nPhysics and Astronomy",
                "Button activated entrances are located on the east and west sides of the building" +
                        "\nThere are accessible restrooms located on all floors" +
                        "\nCentrally located elevators provide access to all floors" +
                        "\nAccessible parking is available to the east (Lot 17 G)",
                "CF 157",
                "CF 21\n" +
                        "CF 24\n" +
                        "CF 26\n" +
                        "CF 161\n" +
                        "CF 165\n" +
                        "CF 167\n" +
                        "CF 312",
                "",
                "",
                "https://www.wwu.edu/building/cf")
        )
        var house5 = BuildingEntity(
            "house5",
            ArrayList<Location>(),
            h5Loc,
            BuildingData(
                "5",
                "5",
                "Residential",
                "Communication Studies\nComputer Science\nJournalism\nPhysics and Astronomy",
                "Button activated entrances are located on the east and west sides of the building" +
                        "\nThere are accessible restrooms located on all floors" +
                        "\nCentrally located elevators provide access to all floors" +
                        "\nAccessible parking is available to the east (Lot 17 G)",
                "CF 157",
                "CF 21\n" +
                        "CF 24\n" +
                        "CF 26\n" +
                        "CF 161\n" +
                        "CF 165\n" +
                        "CF 167\n" +
                        "CF 312",
                "",
                "",
                "https://www.wwu.edu/building/cf")
        )

        var academicWest = BuildingEntity(
            "Academic West",
            ArrayList<Location>(),
            awLoc,
            BuildingData(
                "Academic West",
                "AW",
                "Educational",
                "Communication Studies\nComputer Science\nJournalism\nPhysics and Astronomy",
                "Button activated entrances are located on the east and west sides of the building" +
                        "\nThere are accessible restrooms located on all floors" +
                        "\nCentrally located elevators provide access to all floors" +
                        "\nAccessible parking is available to the east (Lot 17 G)",
                "CF 157",
                "CF 21\n" +
                        "CF 24\n" +
                        "CF 26\n" +
                        "CF 161\n" +
                        "CF 165\n" +
                        "CF 167\n" +
                        "CF 312",
                "",
                "",
                "https://www.wwu.edu/building/aw")
        )

        var environmentalStudies = BuildingEntity(
            "Environmental Studies",
            ArrayList<Location>(),
            esLoc,
            BuildingData(
                "Environmental Studies",
                "AW",
                "Educational",
                "Environmental Studies\nComputer Science\nJournalism\nPhysics and Astronomy",
                "Button activated entrances are located on the east and west sides of the building" +
                        "\nThere are accessible restrooms located on all floors" +
                        "\nCentrally located elevators provide access to all floors" +
                        "\nAccessible parking is available to the east (Lot 17 G)",
                "CF 157",
                "CF 21\n" +
                        "CF 24\n" +
                        "CF 26\n" +
                        "CF 161\n" +
                        "CF 165\n" +
                        "CF 167\n" +
                        "CF 312",
                "",
                "",
                "https://www.wwu.edu/building/es")
        )


        fun initialize(activity: AppCompatActivity) {
            cfLoc.latitude = 48.7327738818
            cfLoc.longitude = -122.485214413

            wkrcLoc.latitude = 48.7315959997
            wkrcLoc.longitude = -122.488958036

            awLoc.latitude = 48.73228015834756
            awLoc.longitude = -122.48651712172678

            esLoc.latitude = 48.73343638692885
            esLoc.longitude =  -122.48551791080642

            h1Loc.latitude = 48.88876780964667
            h1Loc.longitude = -122.47953104095755

            h2Loc.latitude = 48.88908458266765
            h2Loc.longitude = -122.47990714754998

            h3Loc.latitude = 48.88906218630042
            h3Loc.longitude = -122.4789534134033

            h4Loc.latitude = 48.88876263392437
            h4Loc.longitude = -122.47895767114501

            h5Loc.latitude = 48.76244375674259
            h5Loc.longitude = -122.45055761431342

            stairLoc.latitude = 48.73259523326817
            stairLoc.longitude = -122.48624102397639

            entityList.add(commFacilityEntity)
            entityList.add(wadeKingEntity)
            entityList.add(academicWest)
            entityList.add(environmentalStudies)
            entityList.add(house1)
            entityList.add(house2)
            entityList.add(house3)
            entityList.add(house4)
            entityList.add(house5)
            entityList.add(stairsToNowhere)

            for (entity in entityList) {
                entity.initNode(activity)
            }
        }
    }
}