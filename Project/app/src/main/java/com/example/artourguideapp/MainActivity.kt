package com.example.artourguideapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_building_info)

        DummyBuildingEntities.initialize()
        val intent = Intent(this, GeospatialActivity::class.java)
        startActivity(intent)
    }

}