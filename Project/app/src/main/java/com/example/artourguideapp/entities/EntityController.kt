package com.example.artourguideapp.entities

/**
 * A class that passes information to the Model class from a specific view class, or vice-versa.
 */
class EntityController
{
    /**
     * Set entities in Model
     *
     * @param entities Desired list of entities
     */
    fun setEntities(entities: MutableList<Entity>) {
        EntityModel.clearEntities()
        EntityModel.setEntities(entities)
    }

    /**
     * Get entities in model
     *
     * @return Model Entity list
     */
    fun getEntities(): MutableList<Entity> {
        return EntityModel.getEntities()
    }

    fun setSearchIndex(searchIndex: Map<String, Array<Int>>) {
        EntityModel.setSearchIndex(searchIndex)
    }

    fun getSearchIndex(): Map<String, Array<Int>> {
        return EntityModel.getSearchIndex()
    }

}