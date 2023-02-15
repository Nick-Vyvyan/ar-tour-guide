package com.example.artourguideapp.entities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.artourguideapp.R

/**
 * This is a custom [EntityDialogFragment] that can be used to display building info.
 *
 * INSTRUCTIONS FOR USE:
 * 1) Construct a [BuildingEntity]
 * 2) Get the [EntityDialogFragment] (.getDialogFragment())
 * 3) Call buildingInfoDialogFragment.show(supportFragmentManager, "custom tag")
 *
 * @constructor Construct a building dialog fragment from a given [BuildingEntity]
 *
 * @param entity Building entity to create this dialog fragment from
 */
class BuildingDialogFragment(entity: BuildingEntity): EntityDialogFragment(entity) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.building_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buildingData = entity.getEntityData() as BuildingData

        /* GET ALL UI ELEMENTS */
        var nameAndCode: TextView = view.findViewById(R.id.entity_name)
        var types: TextView = view.findViewById(R.id.types)
        var departments: TextView = view.findViewById(R.id.departments)
        var accessibilityLayout: LinearLayout = view.findViewById(R.id.accessibilityLayout)
        var genderNeutralRestrooms: TextView = view.findViewById(R.id.genderNeutralRestrooms)
        var computerLabs: TextView = view.findViewById(R.id.computerLabs)

        var dining: TextView = view.findViewById(R.id.dining)

        /* SET ALL UI ELEMENTS */
        nameAndCode.text = buildingData.getTitle() + " (" + buildingData.getCode() + ")"
        types.text = buildingData.getTypes()
        departments.text = buildingData.getDepartments()

        //accessibilityLayout.removeAllViews()
        var accessibilityInfo: String = buildingData.getAccessibilityInfo()

        // CURRENT METHOD OF PARSING ACCESSIBILITY INFO. MAY CHANGE
        if (accessibilityInfo != "") {
            var accessibilitySections = accessibilityInfo.split("\n")
            for (section in accessibilitySections) {
                var newView = TextView(activity)
                newView.textSize = 18f
                newView.text = "-    $section"
                accessibilityLayout.addView(newView)
            }
        }

        genderNeutralRestrooms.text = buildingData.getGenderNeutralRestrooms()
        computerLabs.text = buildingData.getComputerLabs()
        dining.text = buildingData.getDining()
    }
}