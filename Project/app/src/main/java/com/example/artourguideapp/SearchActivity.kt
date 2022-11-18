package com.example.artourguideapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.artourguideapp.entities.Entity

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val structureList = intent.getSerializableExtra("structureList") as List<Entity>

        // insert college list fragment programmatically
        val detailsFragment = EntityListFragment.newInstance(structureList)

        // create fragment
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainerView, detailsFragment, null)
            commit()
        }
    }
}