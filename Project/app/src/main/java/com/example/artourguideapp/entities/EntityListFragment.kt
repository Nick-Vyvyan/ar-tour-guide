package com.example.artourguideapp.entities

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.artourguideapp.R
import com.example.artourguideapp.SearchActivity


/**
 * A fragment representing a list of Items.
 */
class EntityListFragment : Fragment() {

    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_entity_list_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }

                // set entity data list and adapter in search activity
                adapter = MyEntityListRecyclerViewAdapter(structures, (activity as SearchActivity))
                adapter?.let { (activity as SearchActivity).setAdapter(it) }
            }
        }
        return view
    }

    companion object {
        private lateinit var structures: List<Entity>

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(param: List<Entity>): EntityListFragment {
            val fragment = EntityListFragment().apply {
                structures = param
            }
            return fragment
        }
    }
}