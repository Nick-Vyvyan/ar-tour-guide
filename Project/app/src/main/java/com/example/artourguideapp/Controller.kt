package com.example.artourguideapp

import com.example.artourguideapp.entities.*

/**
 * A class that passes information to the Model class from a specific view class, or vice-versa.
 */
class Controller
{
    /**
     * Set entities in Model
     *
     * @param entities Desired list of entities
     */
    fun setEntities(entities: MutableList<Entity>) {
        Model.clearEntities()
        Model.setEntities(entities)
    }

    /**
     * Get entities in model
     *
     * @return Model Entity list
     */
    fun getEntities(): MutableList<Entity> {
        return Model.getEntities()
    }

    fun setSearchIndex(searchIndex: Map<String, Array<Int>>) {
        Model.setSearchIndex(searchIndex)
    }

    fun getSearchIndex(): Map<String, Array<Int>> {
        return Model.getSearchIndex()
    }

}