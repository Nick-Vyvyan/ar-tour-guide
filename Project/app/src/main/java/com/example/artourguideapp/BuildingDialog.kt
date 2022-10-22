package com.example.artourguideapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class BuildingDialog : AppCompatActivity() {
    lateinit var commFacilityButton: Button
    lateinit var wadeKingButton: Button

    //lateinit var buildingInfoDialog: BuildingInfoDialog

    lateinit var commFacility : BuildingData

    lateinit var wadeKing : BuildingData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_building_info)
        initializeButtons()
        initializeDummyBuildingData()
    }

    private fun initializeButtons() {
        commFacilityButton = findViewById(R.id.commFacilityButton)
        commFacilityButton.setOnClickListener {
            var buildingInfoDialogFragment = BuildingInfoDialogFragment(commFacility)
            buildingInfoDialogFragment.show(supportFragmentManager, "Comm Facility")
        }

        wadeKingButton = findViewById(R.id.wadeKingButton)
        wadeKingButton.setOnClickListener {
            var buildingInfoDialogFragment = BuildingInfoDialogFragment(wadeKing)
            buildingInfoDialogFragment.show(supportFragmentManager, "Wade King")

        }
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

}