package com.example.artourguideapp.entities

import android.content.Context
import android.graphics.PointF
import android.location.Location
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.DataOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

/**
 * This class serves as a placeholder and/or alternative for the BuildingEntities loaded in
 * from the structures JSON file.
 */
class DummyEntities{
    companion object {
        var cfLoc = LatLng(48.7327738818, -122.485214413)
        var wkrcLoc = LatLng(48.7315959997, -122.488958036)
        var awLoc = LatLng(48.73228015834756, -122.48651712172678)
        var esLoc = LatLng(48.73343638692885, -122.48551791080642)

        var h1Loc = LatLng(48.88876780964667, -122.47953104095755)
        var h2Loc = LatLng(48.88908458266765, -122.47990714754998)
        var h3Loc = LatLng(48.88906218630042, -122.4789534134033)
        var h4Loc = LatLng(48.88876263392437, -122.47895767114501)
        var h5Loc = LatLng(48.76244375674259, -122.45055761431342)
        var shLoc = LatLng(48.7458610000001, -122.440000001)
        var stairLoc = LatLng(48.73259523326817, -122.48624102397639)

        var commFacilityEntity = BuildingEntity(
            cfLoc,
            BuildingData(
            "Communications Facility", "CF",
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
            "https://www.wwu.edu/building/cf"),
            1
        )

        var wadeKingEntity = BuildingEntity(
        wkrcLoc,
                BuildingData(
        "Wade King Recreation Center", "SV",
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
        "https://www.wwu.edu/building/sv"),
        2
        )

        var handel = LandmarkEntity( LatLng(0.0, 0.0), LandmarkData(
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
        ),
            12
        )

        var stairsToNowhere = LandmarkEntity(
            stairLoc,
            LandmarkData(
                "Stairs to Nowhere",
                "tellus cras adipiscing enim eu turpis egestas pretium aenean pharetra magna ac placerat vestibulum lectus",
                "",
                "https://westerngallery.wwu.edu/mark-di-suvero-handel-1975"
            ),
            13
        )


        var house1 = BuildingEntity(
            h1Loc,
            BuildingData(
                "house1", "CF",
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
                "https://www.wwu.edu/building/cf"),
            3
        )

        var house2 = BuildingEntity(
            h2Loc,
            BuildingData(
                "house2", "AH",
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
                "https://www.wwu.edu/building/cf"),
            4
        )

        var house3 = BuildingEntity(
            h3Loc,
            BuildingData(
                "3", "VU",
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
                "https://www.wwu.edu/building/cf"),
            5
        )

        var house4 = BuildingEntity(
            h4Loc,
            BuildingData(
                "4", "PH",
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
                "https://www.wwu.edu/building/cf"),
            6
        )
        var house5 = BuildingEntity(
            h5Loc,
            BuildingData(
                "5", "CF",
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
                "https://www.wwu.edu/building/cf"),
            7
        )

        var academicWest = BuildingEntity(
            awLoc,
            BuildingData(
                "Academic West", "AW",
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
                "https://www.wwu.edu/building/aw"),
            8
        )

        var environmentalStudies = BuildingEntity(
            esLoc,
            BuildingData(
                "Environmental Studies", "ES",
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
                "https://www.wwu.edu/building/es"),
            9
        )

        var slowhouse = BuildingEntity(
            shLoc,
            BuildingData(
                "Slowhouse", "ES",
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
                "https://www.wwu.edu/building/es"),
            0
        )

        private var entityArray =
            mutableListOf(commFacilityEntity,
                wadeKingEntity,
                academicWest,
                environmentalStudies,
                house1, house2, house3, house4, house5,
                stairsToNowhere, slowhouse)
        var entityList = ArrayList<Entity>(entityArray)

        fun initialize(activity: AppCompatActivity) {
            for (entity in entityList) {
                entity.initNode(activity)
            }
        }

        suspend fun downloadDummyAudio(context: Context) {
            withContext(Dispatchers.IO) {
                try {

                    val audioFileName = "8a0fa330-7995-4323-a489-86408aadb6f6.mpeg"
                    val connection = URL("https://artourguide.s3.us-west-2.amazonaws.com/$audioFileName")
                        .openConnection() as HttpURLConnection

                    // set remote json string
                    val audioData = connection.inputStream.readBytes()

                    val outputStreamWriter =
                        DataOutputStream(context?.openFileOutput(audioFileName, AppCompatActivity.MODE_PRIVATE))
                    outputStreamWriter.write(audioData)
                    outputStreamWriter.close()

                    Log.d("DEBUG", "Downloaded Audio")
                } catch (ioException: IOException) {
                    ioException.printStackTrace()
                }
            }
        }


    }
}
