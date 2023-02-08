package com.example.artourguideapp

import com.google.ar.core.Config
import com.google.ar.sceneform.math.Vector3

class AppSettings {
    companion object {

        /** Permissions */
        const val PERMISSION_REQUEST_CODE = 12345

        /** Startup */
        const val LOAD_DUMMY_ENTITIES = true
//        const val LOAD_DUMMY_ENTITIES: Boolean = false

        /** Anchors */
        const val ANCHOR_PROXIMITY_DISTANCE = 500f
        const val INITIAL_ANCHOR_SET_INTERVAL_MS : Long = 100
        const val ANCHOR_SET_INTERVAL_MS : Long = 10000

        /** AR Session */
        const val VISIBLE_NODE_PROXIMITY_DISTANCE = 150f
        val AR_UPDATE_MODE = Config.UpdateMode.LATEST_CAMERA_IMAGE
        val AR_LIGHT_ESTIMATION_MODE = Config.LightEstimationMode.ENVIRONMENTAL_HDR
        val AR_GEOSPATIAL_MODE = Config.GeospatialMode.ENABLED
        val AR_PLANE_FINDING_MODE = Config.PlaneFindingMode.DISABLED

        /** Navigation */
        const val NAVIGATION_UPDATE_INTERVAL: Long = 2000
        const val NAVIGATION_UPDATE_DELAY: Long = 2000
        const val USER_WITHIN_WAYPOINT_RADIUS = 15f
        const val USER_WITHIN_DESTINATION_RADIUS = 5f

        /** EntityNode */
        const val ENTITY_VERTICAL_DISPLACEMENT = 5f
        const val ENTITY_VERTICAL_DISPLACEMENT_UPDATE_TOLERANCE = 2f
        const val ENTITY_SCALE_MULTIPLIER = 0.5f
        const val ENTITY_SCALE_MAX_DISTANCE = 50
        val ENTITY_MAX_SCALE =
            Vector3(
                ENTITY_SCALE_MAX_DISTANCE * ENTITY_SCALE_MULTIPLIER,
                ENTITY_SCALE_MAX_DISTANCE * ENTITY_SCALE_MULTIPLIER,
                ENTITY_SCALE_MAX_DISTANCE * ENTITY_SCALE_MULTIPLIER)


        /** NavigationArrowNode */
        val ARROW_NODE_LOCAL_POSITION = Vector3(0f, -.175f, -.5f)
        val ARROW_NODE_LOCAL_SCALE = Vector3(.25f, .25f, .25f)
        val ARROW_NODE_ARROW_OFFSET = Vector3(0f, 0f, 0f)
        val ARROW_NODE_TEXT_OFFSET = Vector3(0f, .15f, .0f)

        /** NavigationWaypointNode */
        val WAYPOINT_SCALE = Vector3(5f, 5f, 5f)
        const val WAYPOINT_VERTICAL_DISPLACEMENT = 5f
    }
}