package com.example.artourguideapp.entities

/**
 * The class that holds all entities
 */
class EntityModel {

    /**
     * Model Companion
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
            Companion.entities = entities
        }

        fun setSearchIndex(searchIndex: Map<String, Array<Int>>) {
            Companion.searchIndex = searchIndex
        }

        fun getSearchIndex(): Map<String, Array<Int>> {
            return searchIndex
        }
    }
}