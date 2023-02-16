package com.example.artourguideapp.entities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.artourguideapp.R

/**
 * This is a custom [EntityDialogFragment] that can be used to display landmark info.
 *
 * INSTRUCTIONS FOR USE:
 * 1) Construct a [LandmarkEntity]
 * 2) Get the [EntityDialogFragment] (.getDialogFragment())
 * 3) Call landmarkInfoDialogFragment.show(supportFragmentManager, "custom tag")
 *
 * @constructor Construct a landmark dialog fragment from a given [LandmarkEntity]
 *
 * @param entity Landmark entity to create this dialog fragment from
 */
class LandmarkDialogFragment(entity: LandmarkEntity): EntityDialogFragment(entity) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.landmark_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val landmarkData = entity.getEntityData() as LandmarkData

        /* GET ALL UI ELEMENTS */
        val name: TextView = view.findViewById(R.id.entity_name)
        val description: TextView = view.findViewById(R.id.landmark_data_description)

        /* SET ALL UI ELEMENTS */
        name.text = landmarkData.getTitle()
        description.text = landmarkData.getDescription()
    }
}