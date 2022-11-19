package com.example.artourguideapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.example.artourguideapp.entities.DummyBuildingEntities
import com.example.artourguideapp.entities.Entity

lateinit var structureListAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
var originalEntities: ArrayList<Entity> = DummyBuildingEntities.entityList
var currentEntities: ArrayList<Entity> = ArrayList()

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // insert entity list fragment programmatically
        DummyBuildingEntities.initialize(this)
        val entityListFragment = EntityListFragment.newInstance(currentEntities)

        // create fragment
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainerView, entityListFragment, null)
            commit()
        }

        // handle search
        currentEntities.addAll(originalEntities)
        findViewById<Button>(R.id.searchButton).setOnClickListener {
            refreshEntityList()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun refreshEntityList() {
        currentEntities.clear()
        currentEntities.addAll(originalEntities)

        val structureName = findViewById<EditText>(R.id.searchName).text.toString()
        val newEntities = originalEntities.filter {
            it.getName().contains(structureName, true)
        } as ArrayList<Entity>

        currentEntities.clear()
        currentEntities.addAll(newEntities)
        structureListAdapter.notifyDataSetChanged()
    }

    // get recyclerview adapter context
    fun setAdapter(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
        structureListAdapter = adapter
    }
}