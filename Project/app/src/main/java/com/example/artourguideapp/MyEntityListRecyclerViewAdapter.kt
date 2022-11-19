package com.example.artourguideapp

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.artourguideapp.placeholder.PlaceholderContent.PlaceholderItem
import com.example.artourguideapp.databinding.FragmentEntityListBinding
import com.example.artourguideapp.entities.Entity

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
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
        holder.idView.text = ""
        holder.contentView.text = item.getName()
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentEntityListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

}