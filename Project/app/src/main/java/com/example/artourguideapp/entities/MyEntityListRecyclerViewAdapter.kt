package com.example.artourguideapp.entities

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView

import com.example.artourguideapp.databinding.FragmentEntityListBinding
import com.example.artourguideapp.R
import com.example.artourguideapp.SearchActivity

/**
 * A [RecyclerView.Adapter] used to display [Entity] objects in a [RecyclerView],
 * primarily used in [SearchActivity].
 *
 * @param values The list of [Entity] objects to display
 * @param activity Used by [SearchActivity] in order to include a class reference
 */
class MyEntityListRecyclerViewAdapter(
    private val values: List<Entity>,
    private val activity: SearchActivity
) : RecyclerView.Adapter<MyEntityListRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentEntityListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.contentView.text =
            if (item is BuildingEntity) item.getName() + " (" + (item.getEntityData()
                    as BuildingData).getCode() + ")"
            else item.getName()

        val structureName = holder.itemView.findViewById<TextView>(R.id.structure_name)
        structureName.setOnClickListener {
            activity.createDialog(item)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentEntityListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val contentView: TextView = binding.structureName

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

}