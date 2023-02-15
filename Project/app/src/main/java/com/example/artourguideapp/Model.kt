package com.example.artourguideapp

import com.example.artourguideapp.entities.*

/**
 * The class that holds all entities
 */
class Model {

    /**
     * The class that holds all entities
     */
    companion object {

        /** Entity list */
        private var entities: MutableList<Entity> = mutableListOf()


        private var searchIndex: Map<String, Array<Int>> = mutableMapOf()

        /**
         * Get entities
         *
         * @return Entity list
         */
        fun getEntities(): MutableList<Entity> {
            return entities
        }

        /**
         * Clear entity list
         */
        fun clearEntities() {
            entities.clear()
        }

        /**
         * Set entities
         *
         * @param entities List of entities to store
         */
        fun setEntities(entities: MutableList<Entity>) {
            Model.entities = entities
        }

        fun setSearchIndex(searchIndex: Map<String, Array<Int>>) {
            Model.searchIndex = searchIndex
        }

        fun getSearchIndex(): Map<String, Array<Int>> {
            return Model.searchIndex
        }
    }
}