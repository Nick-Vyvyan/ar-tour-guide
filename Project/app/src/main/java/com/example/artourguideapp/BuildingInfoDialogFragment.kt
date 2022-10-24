package com.example.artourguideapp

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.fragment.app.DialogFragment

/**
 * This is a custom Dialog that can be used to display building info
 *
 * To use:
 *      Construct a BuildingEntity
 *      Get the BuildingEntity's Dialog Fragment (.getDialogFragment())
 *      call buildingInfoDialogFragment.show(supportFragmentManager, "custom tag")
 */
class BuildingInfoDialogFragment(var buildingData: BuildingData): DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView = inflater.inflate(R.layout.building_info, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* GET ALL UI ELEMENTS */
        var nameAndCode: TextView = view.findViewById(R.id.buildingNameAndCode)
        var buildingScrollView : ScrollView = view.findViewById(R.id.buildingScrollView)
        var types: TextView = view.findViewById(R.id.buildingTypes)
        var departments: TextView = view.findViewById(R.id.buildingDepartments)
        var accessibilityLayout: LinearLayout = view.findViewById(R.id.accessibilityLayout)
        var genderNeutralRestrooms: TextView = view.findViewById(R.id.genderNeutralRestrooms)
        var computerLabs: TextView = view.findViewById(R.id.computerLabs)
        var parkingInfo: TextView = view.findViewById(R.id.parkingInfo)
        parkingInfo.isClickable = true
        parkingInfo.movementMethod = LinkMovementMethod.getInstance()
        var dining: TextView = view.findViewById(R.id.dining)
        var additionalInfo: TextView = view.findViewById(R.id.additionalInfo)
        additionalInfo.isClickable = true
        additionalInfo.movementMethod = LinkMovementMethod.getInstance()


        /* SET ALL UI ELEMENTS */
        nameAndCode.text = buildingData.getTitle() + " (" + buildingData.getCode() + ")"
        types.text = buildingData.getTypes()
        departments.text = buildingData.getDepartments()

        accessibilityLayout.removeAllViews()
        var accessibilityInfo: String = buildingData.getAccessibilityInfo()
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
        parkingInfo.text = buildingData.getParkingInfo()
        dining.text = buildingData.getDining()
        var hyperlinkText = "<a href='" + buildingData.getURL() + "'> Link to website </a>"
        additionalInfo.text = HtmlCompat.fromHtml(hyperlinkText, HtmlCompat.FROM_HTML_MODE_COMPACT)

        buildingScrollView.scrollY = 0
    }
}