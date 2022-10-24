package com.example.artourguideapp

import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

/**
 * DUMMY CLASS TO TEST BUILDING DIALOG FRAGMENTS
 */
class BuildingDialog : AppCompatActivity() {
    lateinit var commFacilityButton: Button
    lateinit var wadeKingButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_building_info)
        initializeButtons()
    }

    private fun initializeButtons() {
        commFacilityButton = findViewById(R.id.commFacilityButton)
        commFacilityButton.setOnClickListener {
            DummyBuildingEntities.commFacilityEntity.getDialogFragment().show(supportFragmentManager, DummyBuildingEntities.commFacilityEntity.getName())
        }

        wadeKingButton = findViewById(R.id.wadeKingButton)
        wadeKingButton.setOnClickListener {
            DummyBuildingEntities.wadeKingEntity.getDialogFragment().show(supportFragmentManager, DummyBuildingEntities.wadeKingEntity.getName())

        }
    }

}