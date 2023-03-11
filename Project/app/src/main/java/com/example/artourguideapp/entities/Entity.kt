package com.example.artourguideapp.entities

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.google.android.gms.maps.model.LatLng


/**
 * An abstract class which is used as the template for [BuildingEntity] and [LandmarkEntity] objects,
 * so that they can be stored and displayed together. Contains Entity data, a location, a custom dialog fragment,
 * and an AR renderable node.
 *
 * @constructor Construct an [Entity]
 *
 * @param name Entity name
 * @param url Entity url
 * @param latLng Entity latitude and longitude
 * @param entityData Entity data
 * @param searchId Entity search ID
 */
abstract class Entity(
    private var name: String,
    private var url: String,
    private var latLng: LatLng,
    private var entityData: EntityData,
    private var searchId: Int,
) {

    private lateinit var dialogFragment: EntityDialogFragment
    private var node: EntityNode? = null
    private var isDestination: Boolean = false

    /**
     * Initialize the AR node inside a given activity. Must be passed the AR activity. This function
     * must be called before attempting to render an [EntityNode]
     *
     * @param activity The AR activity to display the node inside
     */
    fun initNode(activity: AppCompatActivity) {
        node = EntityNode(activity, this)
    }

    /**
     * Get whether this objects EntityNode is attached or not
     *
     * @return True if the node has been attached (node and parent are not null), false if not.
     */
    fun nodeIsAttached() : Boolean {
        return node != null && node!!.isActive
    }

    /**
     * Get name
     *
     * @return Entity name
     */
    fun getName(): String {
        return name
    }

    /**
     * Get url
     *
     * @return Entity url
     */
    fun getURL(): String {
        return url
    }

    /**
     * Get AR node
     *
     * @return Entity AR node
     */
    fun getNode() : EntityNode? {
        return node
    }

    /**
     * Get search id
     *
     * @return search id
     */
    fun getSearchId() : Int {
        return searchId
    }

    /**
     * Get entity's latitude and longitude
     *
     * @return LatLng of this entity
     */
    fun getLatLng() : LatLng
    {
        return latLng
    }

    /**
     * Set latitude and longitude
     *
     * @param latitude desired latitude [-90, 90]
     * @param longitude desired longitude [-180, 180]
     * @return
     */
    fun setLatLng(latitude : Double, longitude : Double)
    {
        val latRange : ClosedRange<Double> = -90.0..90.0
        val lonRange : ClosedRange<Double> = -180.0..180.0
        if (latitude in latRange && longitude in lonRange)
        {
            latLng = LatLng(latitude, longitude)

        }
        else
        {
            error("invalid latitude/longitude value in setLocation")
        }
    }

    /**
     * Get dialog fragment
     *
     * @return Entity dialog fragment
     */
    fun getDialogFragment(): EntityDialogFragment {
        return dialogFragment
    }

    /**
     * Internally set dialog fragment
     *
     * @param dialogFragment Desired dialog fragment for this Entity
     */
    protected fun setDialogFragment(dialogFragment: EntityDialogFragment) {
        this.dialogFragment = dialogFragment
    }

    /**
     * Get entity data
     *
     * @return Entity data
     */
    fun getEntityData(): EntityData {
        return entityData
    }

    /**
     * Set as destination (change isDestination bool to true)
     */
    fun setAsDestination() {
        isDestination = true
    }

    /**
     * Clear as destination (change isDestination bool to false)
     */
    fun clearAsDestination() {
        isDestination = false
    }

    /**
     * Returns whether this Entity is the current destination or not
     *
     * @return True if this entity is the destination, false if not
     */
    fun isDestination(): Boolean {
        return isDestination
    }
}