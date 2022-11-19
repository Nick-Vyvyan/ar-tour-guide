package com.example.artourguideapp.entities

import android.app.Activity
import android.content.Context
import android.location.Location
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.google.ar.core.Anchor
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.rendering.Color
import com.google.ar.sceneform.rendering.ViewRenderable

abstract class Entity(
    private var name: String,
    private var perimeter: MutableList<Location>,
    private var url: String,
    private var centralLocation : Location,
    private var entityData: EntityData,
    private var dialogFragment: DialogFragment,
) {
//    lateinit var anchor: Anchor
    private var node: Node = Node()

    fun initNode(activity: AppCompatActivity) {
//        node = Node()
        node.name = name;

        // Programmatically build an ar button without a XML
        val arButton = Button(activity)
        arButton.text = name
//        arButton.setPadding(3, 0, 3, 0)
        arButton.setBackgroundColor(0xffae3de3.toInt())
        arButton.setOnClickListener {
            dialogFragment.show(activity.supportFragmentManager, name)
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

    fun getPerimeter(): MutableList<Location> {
        return perimeter
    }

    fun setPerimeter(points: MutableList<Location>) {
        perimeter = points
    }

    fun getURL(): String {
        return url
    }

//    fun setNode(anchor: Anchor) {
////        this.anchor = anchor
//    }

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