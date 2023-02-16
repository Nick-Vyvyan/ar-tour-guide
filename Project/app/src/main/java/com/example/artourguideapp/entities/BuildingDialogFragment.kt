package com.example.artourguideapp.entities

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.text.HtmlCompat
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
        val nameAndCode: TextView = view.findViewById(R.id.entity_name)

        val types: TextView = view.findViewById(R.id.types)
        val typesLabel: TextView = view.findViewById(R.id.typesLabel)

        val departmentsLayout: LinearLayout = view.findViewById(R.id.departmentsLayout)
        val departmentsLabel: TextView = view.findViewById(R.id.departmentsLabel)

        val genderNeutralRestrooms: TextView = view.findViewById(R.id.genderNeutralRestrooms)
        val genderNeutralRestroomsLabel: TextView = view.findViewById(R.id.genderNeutralRestroomsLabel)

        val computerLabs: TextView = view.findViewById(R.id.computerLabs)
        val computerLabsLabel: TextView = view.findViewById(R.id.computerLabsLabel)

        val diningLayout: LinearLayout = view.findViewById(R.id.diningLayout)
        val diningLabel: TextView = view.findViewById(R.id.diningLabel)

        val accessibilityLayout: LinearLayout = view.findViewById(R.id.accessibilityLayout)
        val accessibilityInfoLabel: TextView = view.findViewById(R.id.accessibilityInfoLabel)

        /* SET ALL UI ELEMENTS */
        val formattedTitle = "${buildingData.getTitle()} (${buildingData.getCode()})"
        nameAndCode.text = formattedTitle

        formatUiForData(typesLabel, types, buildingData.getTypes())
        formatUiForData(genderNeutralRestroomsLabel, genderNeutralRestrooms, buildingData.getGenderNeutralRestrooms())
        formatUiForData(computerLabsLabel, computerLabs, buildingData.getComputerLabs())
        formatDepartments(departmentsLabel, departmentsLayout, buildingData.getDepartments())
        formatAccessibility(accessibilityLayout, accessibilityInfoLabel, buildingData.getAccessibilityInfo())
        formatDining(diningLabel, diningLayout, buildingData.getDining())
    }

    /**
     * Format the UI. If the data is blank, remove that section from the UI. Otherwise, set the text.
     *
     * @param labelView Label TextView
     * @param dataView Data TextView
     * @param data Data to test
     */
    private fun formatUiForData(labelView: TextView, dataView: TextView, data: String) {
        if (data.isNotBlank()) {
            dataView.text = data
        }
        else {
            labelView.visibility = View.GONE
            dataView.visibility = View.GONE
        }
    }

    /**
     * Format departments on UI. If the data is blank, remove that section from the UI. Otherwise, set the text.
     *
     * @param departmentsLabel Departments label
     * @param departmentsLayout Departments layout
     * @param departments Departments data
     */
    private fun formatDepartments(departmentsLabel: TextView, departmentsLayout: LinearLayout, departments: String) {
        // If not empty
        if (departments.isNotBlank()) {
            // Set left pad to 20dp and top pad to 10dp
            val density = resources.displayMetrics.density
            val leftPad = (20 * density).toInt()
            val topPad = (10 * density).toInt()

            // Split dining data into each dining option and iterate
            val departmentsArray = departments.split(",")
            var i = 0
            while (i < departmentsArray.size) {
                val departmentTextView = TextView(activity)

                // Trim and replace incorrect substrings with apostrophes
                var department = departmentsArray[i].trim().replace("&#039;", "'")

                // If department is "Women", this is an edge case due to department of
                // "Women, Gender, and Sexuality Studies" being split earlier. Get next two elements
                // of the array and concatenate for this department
                if (department == "Women") {
                    department = "$department, ${departmentsArray[++i].trim()}, ${departmentsArray[++i].trim()}"
                }

                // Set padding, size, and text. Then add to layout
                departmentTextView.setPadding(leftPad, topPad, 0, 0)
                departmentTextView.textSize = 18f
                departmentTextView.text = department
                departmentsLayout.addView(departmentTextView)

                i++
            }

        }
        // If empty, remove
        else {
            departmentsLabel.visibility = View.GONE
            departmentsLayout.visibility = View.GONE
        }
    }

    /**
     * Format accessibility on UI. Each section is displayed with a preceding hyphen.
     * If the data is blank, remove that section from the UI. Otherwise, set the text.
     *
     * @param accessibilityLayout Accessibility layout
     * @param accessibilityInfoLabel Accessibility info label
     * @param accessibilityInfo Accessibility info
     */
    private fun formatAccessibility(accessibilityLayout: LinearLayout, accessibilityInfoLabel: TextView, accessibilityInfo: String) {
        // If non-empty info
        if (accessibilityInfo.isNotBlank()) {

            // Set left pad to 20dp and top pad to 10dp
            val density = resources.displayMetrics.density
            val leftPad = (20 * density).toInt()
            val topPad = (10 * density).toInt()

            // Split into sections and iterate
            val accessibilitySections = accessibilityInfo.split(",")
            for (section in accessibilitySections) {

                // Each section gets its own horizontal linear layout so that hyphens and text are aligned
                val newLayout = LinearLayout(context)
                newLayout.orientation = LinearLayout.HORIZONTAL

                newLayout.setPadding(leftPad, topPad, 0, 0)

                // Create the hyphen TextView and section TextView
                val hyphenView = TextView(activity)
                val accessibilitySectionView = TextView(activity)

                // Set text size
                hyphenView.textSize = 18f
                accessibilitySectionView.textSize = 18f

                // Set text
                hyphenView.text = "-    "
                accessibilitySectionView.text = section.trim()

                // Add to new linear layout
                newLayout.addView(hyphenView)
                newLayout.addView(accessibilitySectionView)

                // Add new linear layout to accessibility layout
                accessibilityLayout.addView(newLayout)
            }
        }
        // Otherwise, remove elements from UI
        else {
            accessibilityLayout.visibility = View.GONE
            accessibilityInfoLabel.visibility = View.GONE
        }
    }

    /**
     * Format dining information. Dining options will link to their website if they have a website.
     * If the data is blank, remove that section from the UI. Otherwise, set the text.
     *
     * @param diningLabel Dining label
     * @param diningLayout Dining layout
     * @param dining Dining data
     */
    private fun formatDining(diningLabel: TextView, diningLayout: LinearLayout, dining: String) {
        // If not empty
        if (dining.isNotBlank() && dining != "None") {

            // Set left pad to 20dp and top pad to 10dp
            val density = resources.displayMetrics.density
            val leftPad = (20 * density).toInt()
            val topPad = (10 * density).toInt()

            // Split dining data into each dining option and iterate
            val diningOptions = dining.split(",")
            for (i in diningOptions.indices) {

                // Get the option tuple
                val option = diningOptions[i]
                val optionTuple = option.split("-")
                val optionName = optionTuple[0].trim()

                // Create the TextView for this option
                val diningView = TextView(context)

                // If the option has a url, link the url
                if (optionTuple.size > 1) {
                    val optionLink = optionTuple[1].trim()
                    val hyperlinkText = "<a href='$optionLink'> $optionName </a>"

                    diningView.text = HtmlCompat.fromHtml(hyperlinkText, HtmlCompat.FROM_HTML_MODE_COMPACT)
                    diningView.isClickable = true
                    diningView.movementMethod = LinkMovementMethod.getInstance()
                }
                // Otherwise, just the name is fine
                else {
                    diningView.text = optionName
                }

                // Set text size and add to layout
                diningView.setPadding(leftPad, topPad, 0, 0)
                diningView.textSize = 18f
                diningLayout.addView(diningView)

            }

        }
        // If empty, remove
        else {
            diningLabel.visibility = View.GONE
            diningLayout.visibility = View.GONE
        }
    }
}