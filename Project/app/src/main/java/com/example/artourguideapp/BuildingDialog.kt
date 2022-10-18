package com.example.artourguideapp

import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.fromHtml

class BuildingDialog : AppCompatActivity() {
    lateinit var commFacilityButton: Button
    lateinit var wadeKingButton: Button

    lateinit var buildingView: View
    lateinit var buildingDialog: Dialog

    lateinit var nameAndCode: TextView

    lateinit var buildingScrollView: ScrollView
    lateinit var types: TextView
    lateinit var departments: TextView
    lateinit var accessibilityLayout: LinearLayout
    lateinit var genderNeutralRestrooms: TextView
    lateinit var computerLabs: TextView
    lateinit var parkingInfo: TextView
    lateinit var dining: TextView
    lateinit var additionalInfo: TextView

    lateinit var commFacility : BuildingData

    lateinit var wadeKing : BuildingData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_building_info)

        initializeButtons()
        initializeBuildingInfoDialog()
        initializeDummyBuildingData()
    }

    fun displayBuildingInfo(buildingData: BuildingData) {
        nameAndCode.text = buildingData.getTitle() + " (" + buildingData.getCode() + ")"
        types.text = buildingData.getTypes()
        departments.text = buildingData.getDepartments()
        displayAccessibility(buildingData.getAccessibilityInfo())
        genderNeutralRestrooms.text = buildingData.getGenderNeutralRestrooms()
        computerLabs.text = buildingData.getComputerLabs()
        parkingInfo.text = buildingData.getParkingInfo()
        dining.text = buildingData.getDining()
        var hyperlinkText = "<a href='" + buildingData.getURL() + "'> Link to website </a>"
        additionalInfo.text = fromHtml(hyperlinkText, HtmlCompat.FROM_HTML_MODE_COMPACT)

        buildingScrollView.scrollY = 0
        buildingDialog.show()
    }

    private fun initializeButtons() {
        commFacilityButton = findViewById<Button>(R.id.commFacilityButton)
        commFacilityButton.setOnClickListener {
            displayBuildingInfo(commFacility)
        }

        wadeKingButton = findViewById<Button>(R.id.wadeKingButton)
        wadeKingButton.setOnClickListener {
            displayBuildingInfo(wadeKing)
        }
    }

    private fun initializeBuildingInfoDialog() {
        var inflater : LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        buildingView = inflater.inflate(R.layout.building_info, null)

        nameAndCode = buildingView.findViewById(R.id.buildingNameAndCode)

        buildingScrollView = buildingView.findViewById(R.id.buildingScrollView)

        types = buildingView.findViewById(R.id.buildingTypes)

        departments = buildingView.findViewById(R.id.buildingDepartments)

        accessibilityLayout = buildingView.findViewById(R.id.accessibilityLayout)

        genderNeutralRestrooms = buildingView.findViewById(R.id.genderNeutralRestrooms)

        computerLabs = buildingView.findViewById(R.id.computerLabs)

        parkingInfo = buildingView.findViewById(R.id.parkingInfo)
        parkingInfo.isClickable = true
        parkingInfo.movementMethod = LinkMovementMethod.getInstance()

        dining = buildingView.findViewById(R.id.dining)

        additionalInfo = buildingView.findViewById(R.id.additionalInfo)
        additionalInfo.isClickable = true
        additionalInfo.movementMethod = LinkMovementMethod.getInstance()

        buildingDialog = Dialog(this)
        buildingDialog.setContentView(buildingView)
        buildingDialog.window?.setLayout((Resources.getSystem().displayMetrics.widthPixels * .9).toInt(), (Resources.getSystem().displayMetrics.heightPixels * .9).toInt())
    }

    private fun initializeDummyBuildingData() {
        commFacility = BuildingData(
            "Communications Facility",
            "CF",
            "Academic",
            "Communication Studies\nComputer Science\nJournalism\nPhysics and Astronomy",
            "Button activated entrances are located on the east and west sides of the building" +
                    "\nThere are accessible restrooms located on all floors" +
                    "\nCentrally located elevators provide access to all floors" +
                    "\nAccessible parking is available to the east (Lot 17 G)",
            "CF 157",
            "CF 21\n" +
                    "CF 24\n" +
                    "CF 26\n" +
                    "CF 161\n" +
                    "CF 165\n" +
                    "CF 167\n" +
                    "CF 312",
            null,
            null,
            "https://www.wwu.edu/building/cf"
        )

        wadeKing = BuildingData(
            "Wade King Recreation Center",
            "SV",
            "Events\nRecreation",
            "Campus Recreation Services\n" +
                    "Sport Clubs",
            "Accessible parking to the northeast (Lot 19G)\n" +
                    "Accessible entrances on the east side\n" +
                    "Accessibility notes:\n" +
                    "\tAccessible rest rooms on the main floor",
            "SV 110, 155, 156",
            null,
            "Rock's Edge Cafe",
            null,
            "https://www.wwu.edu/building/sv"
        )
    }

    private fun displayAccessibility(accessibilityInfo: String?) {
        accessibilityLayout.removeAllViews()

        if (accessibilityInfo != null) {
            var accessibilitySections = accessibilityInfo.split("\n")
            for (section in accessibilitySections) {
                var newView = TextView(this)
                newView.textSize = 18f
                newView.text = "-    $section"
                accessibilityLayout.addView(newView)
            }
        }
    }

}