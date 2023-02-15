package com.example.artourguideapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.artourguideapp.entities.*

/**
 * Allows the user to search for structures by name, using a search bar and dynamic list of structures.
 *
 * When a structure name is clicked on, either a [BuildingDialogFragment] or [LandmarkDialogFragment]
 * is shown, depending on the type of structure, represented as an [Entity] object.
 */
class SearchActivity : AppCompatActivity() {

    companion object {
        lateinit var structureListAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
        var controller: Controller = Controller()
        var originalEntities: ArrayList<Entity> = ArrayList(controller.getEntities())
        var currentEntities: ArrayList<Entity> = ArrayList()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // insert entity list fragment programmatically
        val entityListFragment = EntityListFragment.newInstance(currentEntities)

        // create fragment
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainerView, entityListFragment, null)
            commit()
        }

        // handle search
        originalEntities = ArrayList(controller.getEntities())

        currentEntities.clear()
        currentEntities.addAll(originalEntities)
        currentEntities.sortBy { it.getName() }

        val searchButton = findViewById<Button>(R.id.searchButton)
        val searchText = findViewById<EditText>(R.id.searchText)

        // handle pressing search button in different ways
        searchButton.setOnClickListener {
            refreshEntityList()
        }
        searchText.setOnEditorActionListener { _, i, _ ->
            return@setOnEditorActionListener when (i) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    refreshEntityList()
                    true
                }
                else -> false
            }
        }
    }

    /**
     * Update list with new [Entity] objects based on the search query provided
     * by the user. If the search query is empty, load in original list.
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun refreshEntityList() {
        currentEntities.clear()
        currentEntities.addAll(originalEntities)

        val searchQuery = findViewById<EditText>(R.id.searchText).text.toString().split(' ')
        val searchIndex = controller.getSearchIndex()
        var searchResults = ArrayList<Int>()

        for (token in searchQuery) {
            // key is in index - init indexRow, else null
            val indexRow = if (searchIndex.containsKey(token)) searchIndex[token] else null

            if (indexRow != null) {
                // add values to results
                searchResults = incrementSearchResults(indexRow, searchResults)
            } else {
                for (word in searchIndex.keys) {
                    if (word.contains(token)) {
                        searchResults = searchIndex[word]?.let { incrementSearchResults(it, searchResults) }!!
                    }
                }
            }
        }

        // filter entities by query
        val newEntities = if (searchQuery.isEmpty())
            originalEntities
        else
            originalEntities.filter {
                if (it.getSearchId() < searchResults.size) searchResults[it.getSearchId()] > 0 else false
            } as ArrayList<Entity>

        newEntities.sortByDescending { item -> searchResults[item.getSearchId()] }
        currentEntities.clear()
        currentEntities.addAll(newEntities)
        currentEntities.sortBy { it.getName() }
        structureListAdapter.notifyDataSetChanged()
    }

    // effectively an AND operation for an array and arraylist, arraylist will grow to size if smaller than array
    private fun incrementSearchResults(indexRow: Array<Int>, searchResults: ArrayList<Int>): ArrayList<Int> {
        for ((i, presence) in indexRow.withIndex()) {
            while (searchResults.size < i+1) {
                searchResults.add(0)
            }
            searchResults[i] += presence
        }

        return searchResults
    }

    // get recyclerview adapter context
    fun setAdapter(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
        structureListAdapter = adapter
    }

    fun createDialog(structure: Entity) {
        structure.getDialogFragment().show(supportFragmentManager, structure.getName())

//        if (structure is BuildingEntity)
//            BuildingDataDialogFragment(structure.getEntityData() as BuildingData, structure.getCentralLocation())
//                .show(supportFragmentManager, structure.getName())
//        else if (structure is LandmarkEntity)
//            LandmarkDialogFragment(structure.getEntityData() as LandmarkData, structure.getCentralLocation())
//                .show(supportFragmentManager, structure.getName())
    }
}
