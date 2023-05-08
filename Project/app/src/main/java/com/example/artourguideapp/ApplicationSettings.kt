package com.example.artourguideapp

import com.google.ar.core.Config
import com.google.ar.sceneform.math.Vector3

/** Application Settings Object Class */
object ApplicationSettings {

    /** Permissions Request Code */
    const val PERMISSION_REQUEST_CODE = 12345

    /** Start with dummy entities (T/F) */
//        const val LOAD_DUMMY_ENTITIES = true
    const val LOAD_DUMMY_ENTITIES: Boolean = false

    //region AnchorHelper

    /** Place any anchors within this distance */
    const val ANCHOR_PROXIMITY_DISTANCE = 500f

    /** Frequency of anchor setting until initial placement (milliseconds) */
    const val INITIAL_ANCHOR_SET_INTERVAL_MS : Long = 100

    /** Frequency of anchor setting after initial placement (milliseconds) */
    const val ANCHOR_SET_INTERVAL_MS : Long = 10000

    /** Maximum accuracy threshold for anchor placement. Don't place anchors if
     * accuracy is greater than this value. from Geospatial API documentation:
     * "We define horizontal accuracy as the radius of the 68th percentile
     * confidence level around the estimated horizontal location. there is a 68%
     * probability that the true location is inside the circle.
     * Larger numbers indicate lower accuracy." */
    const val ACCURACY_MAX_THRESHOLD = 17.5f

    //endregion

    //region AR Session

    /** The distance an AR node must be within to appear on screen */
    const val AR_VISIBILITY_DISTANCE = 150f

    /** AR UpdateMode */
    val AR_UPDATE_MODE = Config.UpdateMode.LATEST_CAMERA_IMAGE

    /** AR LightEstimationMode */
    val AR_LIGHT_ESTIMATION_MODE = Config.LightEstimationMode.ENVIRONMENTAL_HDR

    /** AR GeospatialMode must be enabled */
    val AR_GEOSPATIAL_MODE = Config.GeospatialMode.ENABLED

    /** AR PlaneFindingMode is disabled to save power */
    val AR_PLANE_FINDING_MODE = Config.PlaneFindingMode.DISABLED

    //endregion

    //region Navigation

    /** Amount of milliseconds that pass between navigation updates */
    const val NAVIGATION_UPDATE_INTERVAL: Long = 2000

    /** Amount of milliseconds that pass before navigation updates start  */
    const val NAVIGATION_UPDATE_DELAY: Long = 0

    /** Distance in meters that the user must be within before the waypoint will update */
    const val USER_WITHIN_WAYPOINT_RADIUS = 15f

    /** Distance in meters that the user must be within before the destination has been reached */
    const val USER_WITHIN_DESTINATION_RADIUS = 5f

    //endregion

    //region EntityNode

    /** The height that EntityNodes will placed at above the user */
    const val ENTITY_HEIGHT = 5f

    /** The amount of height the user is allowed to lose or gain before entity height resets */
    const val ENTITY_HEIGHT_UPDATE_TOLERANCE = 2f

    /** The maximum distance to apply EntityNode scaling before it is set to MAX_SCALE */
    const val ENTITY_SCALE_MAX_DISTANCE = 50

    /** The multiplier used with distance to scale the EntityNode */
    const val ENTITY_SCALE_MULTIPLIER = 0.5f

    /** The maximum scale of an EntityNode (set to MAX_DISTANCE * SCALE_MULTIPLIER) */
    val ENTITY_MAX_SCALE = Vector3(ENTITY_SCALE_MAX_DISTANCE * ENTITY_SCALE_MULTIPLIER,
                                   ENTITY_SCALE_MAX_DISTANCE * ENTITY_SCALE_MULTIPLIER,
                                   ENTITY_SCALE_MAX_DISTANCE * ENTITY_SCALE_MULTIPLIER)

    //endregion

    //region NavigationArrowNode

    /** Position that the navigation arrow node is placed */
    val ARROW_NODE_LOCAL_POSITION = Vector3(0f, -.175f, -.5f)

    /** Scale of the navigation arrow node */
    val ARROW_NODE_LOCAL_SCALE = Vector3(.25f, .25f, .25f)

    /** Offset of the arrow model in the arrow node */
    val ARROW_NODE_ARROW_OFFSET = Vector3(0f, 0f, 0f)

    /** Offset of the waypoint text in the arrow node */
    val ARROW_NODE_TEXT_OFFSET = Vector3(0f, .15f, .0f)

    //endregion

    //region NavigationWaypointNode

    /** Scale of the waypoint arrow */
    val WAYPOINT_SCALE = Vector3(5f, 5f, 5f)

    /** Height of the waypoint arrow */
    const val WAYPOINT_HEIGHT = 5f

    //endregion

    //region EntityDialog

    /** Dialog size as a percentage of screen size */
    const val DIALOG_SIZE_PERCENTAGE_OF_SCREEN_WIDTH = .95f

    /** Dialog size as a percentage of screen size */
    const val DIALOG_SIZE_PERCENTAGE_OF_SCREEN_HEIGHT = .95f

    //endregion
}
