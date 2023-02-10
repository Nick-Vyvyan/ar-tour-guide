package com.example.artourguideapp.entities

import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.artourguideapp.AppSettings
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

        val correctHeight =  scene!!.camera!!.worldPosition!!.y + AppSettings.ENTITY_HEIGHT
        worldPosition = Vector3(worldPosition.x, correctHeight, worldPosition.z)
    }

    /** Update rotation and scale per frame*/
    override fun onUpdate(frameTime: FrameTime?) {
        super.onUpdate(frameTime)

        if (scene == null || scene?.camera == null) {
            return
        }

        updatePosition()
        updateRotation()
        updateScale()
    }

    /**
     * Updates node position if user has gained or lost ENTITY_HEIGHT_UPDATE_TOLERANCE meters in height.
     * i.e. User is at 2 meters height, Entity height is 5 meters and tolerance is 2 meters. When user height becomes
     * 0 meters or 4 meters, the entity height will update to reflect that
     */
    private fun updatePosition() {
        if (scene == null || scene?.camera == null || scene?.camera?.worldPosition  == null) {
            return
        }

        val userPositionHeight = scene!!.camera!!.worldPosition!!.y
        val maxPositionUpdateHeight = worldPosition.y - AppSettings.ENTITY_HEIGHT + AppSettings.ENTITY_HEIGHT_UPDATE_TOLERANCE
        val minPositionUpdateHeight = worldPosition.y - AppSettings.ENTITY_HEIGHT - AppSettings.ENTITY_HEIGHT_UPDATE_TOLERANCE

        if (userPositionHeight > maxPositionUpdateHeight || userPositionHeight < minPositionUpdateHeight) {
            val correctHeight =  scene!!.camera!!.worldPosition!!.y + AppSettings.ENTITY_HEIGHT
            worldPosition = Vector3(worldPosition.x, correctHeight, worldPosition.z)
        }
    }

    /** Scale is updated according to current distance from user */
    private fun updateScale() {
        val distance = Vector3.subtract(worldPosition, scene!!.camera.worldPosition).length()

        // If current distance < max scale distance, scale it
        worldScale = if (distance  < AppSettings.ENTITY_SCALE_MAX_DISTANCE)
                Vector3(
                distance * AppSettings.ENTITY_SCALE_MULTIPLIER,
                distance * AppSettings.ENTITY_SCALE_MULTIPLIER,
                distance * AppSettings.ENTITY_SCALE_MULTIPLIER
            )

            // Otherwise set to default scale
            else
                AppSettings.ENTITY_MAX_SCALE
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