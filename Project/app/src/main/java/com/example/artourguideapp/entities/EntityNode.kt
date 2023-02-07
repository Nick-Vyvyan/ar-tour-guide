package com.example.artourguideapp.entities

import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.artourguideapp.AnchorHelper
import com.example.artourguideapp.R
import com.example.artourguideapp.navigation.Navigation
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ViewRenderable

/** An AR Node that corresponds to a given Entity */
class EntityNode(activity: AppCompatActivity, private var entity: Entity): Node() {

    /** Set the node name and parent, then create the AR button. */
    init {
        name = entity.getName()
        parent = null

        createButton(activity)
    }


    override fun onActivate() {
        super.onActivate()

        if (scene == null) {
            throw IllegalStateException("AR Scene is null!")
        }
    }

    /** Update rotation and scale per frame*/
    override fun onUpdate(frameTime: FrameTime?) {
        super.onUpdate(frameTime)

        if (scene == null || scene?.camera == null) {
            return
        }

        updateRotation()
        updateScale()
    }

    /** Scale is updated according to current distance from user */
    private fun updateScale() {
        val distance = Vector3.subtract(worldPosition, scene!!.camera.worldPosition).length()

        // If current distance < max scale distance, scale it
        worldScale = if (distance  < AnchorHelper.SCALE_MAX_DISTANCE)
                Vector3(
                distance * AnchorHelper.SCALE_MULTIPLIER,
                distance * AnchorHelper.SCALE_MULTIPLIER,
                distance * AnchorHelper.SCALE_MULTIPLIER
            )

            // Otherwise set to default scale
            else
                Vector3(
                    AnchorHelper.DEFAULT_SCALE,
                    AnchorHelper.DEFAULT_SCALE,
                    AnchorHelper.DEFAULT_SCALE
                )
    }

    /** Rotation is updated to look at user */
    private fun updateRotation() {
        val direction = Vector3.subtract(worldPosition, scene!!.camera.worldPosition)
        val rotation = Quaternion.lookRotation(direction, Vector3.up())
        worldRotation = rotation
    }

    /** Create the AR button */
    private fun createButton(activity: AppCompatActivity) {

        val arButton = Button(activity)

        arButton.setBackgroundResource(R.drawable.button_rounded_corners)
        arButton.isHapticFeedbackEnabled = true
        arButton.text = entity.getName()
        arButton.setTextAppearance(R.style.ButtonText)

        arButton.setOnClickListener {
            if (!entity.getDialogFragment().isVisible) {
                entity.getDialogFragment().show(activity.supportFragmentManager, name)
            }
            if (entity.isDestination()) {
                Navigation.stopNavigation()
            }

        }

        ViewRenderable.builder().setView(activity, arButton).build()
            .thenAccept {renderable ->
                this.renderable = renderable }
    }
}