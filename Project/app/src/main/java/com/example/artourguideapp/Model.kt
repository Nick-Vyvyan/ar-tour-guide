package com.example.artourguideapp

import com.example.artourguideapp.entities.*

/**
 * A class that stores structure information passed from a specific view class through a Controller.
 */
class Model {

    companion object {
        private var entities: MutableList<Entity> = mutableListOf()
        private var searchIndex: Map<String, Array<Int>> = mutableMapOf()


        fun getEntities(): MutableList<Entity> {
            return entities
        }

        fun clearEntities() {
            entities = mutableListOf()
        }

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