package com.example.artourguideapp.entities

import android.graphics.PointF
import android.location.Location
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import com.example.artourguideapp.R
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.rendering.ViewRenderable


/**
 * An abstract class which is used as the template for BuildingEntity and LandmarkEntity objects,
 * so that they can be stored and displayed together.
 */
abstract class Entity(
    private var name: String,
    private var url: String,
    private var centralLocation : Location,
    private var entityData: EntityData,
    private var dialogFragment: DialogFragment,
) {
//    lateinit var anchor: Anchor
    private var node: Node = Node()

    fun initNode(activity: AppCompatActivity) {
//        node = Node()
        node.name = name
        node.parent = null

        // Programmatically build an ar button without a XML
        val arButton = Button(activity)

        arButton.setBackgroundColor(ResourcesCompat.getColor(activity.resources, R.color.wwu_blue, null))
        arButton.setTextColor(ResourcesCompat.getColor(activity.resources, R.color.wwu_white, null))
        arButton.text = name

        arButton.setOnClickListener {
            if (!dialogFragment.isVisible) {
                dialogFragment.show(activity.supportFragmentManager, name)
            }

        }

        ViewRenderable.builder().setView(activity, arButton).build()
            .thenAccept {renderable ->
                node.renderable = renderable }
    }

    fun nodeIsAttached() : Boolean {
        return node != null && node.parent != null
    }

    fun getName(): String {
        return name
    }

    fun getURL(): String {
        return url
    }

    fun getNode() : Node {
        return node
    }


    fun getCentralLocation() : Location
    {
        return centralLocation
    }

    fun setLocation(latitude : Double, longitude : Double) : Int
    {
        val latRange : ClosedRange<Double> = -90.0..90.0
        val lonRange : ClosedRange<Double> = -180.0..180.0
        if (latitude in latRange && longitude in lonRange)
        {
            centralLocation.latitude = latitude
            centralLocation.longitude = longitude
        }
        else
        {
            error("invalid latitude/longitude value in setLocation")
        }
        return 0

    }

    fun getDialogFragment(): DialogFragment {
        return dialogFragment
    }

    fun getEntityData(): EntityData {
        return entityData
    }

}