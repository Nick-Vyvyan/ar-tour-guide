package com.example.artourguideapp

import com.example.artourguideapp.entities.*

/**
 * A class that stores structure information passed from a specific view class through a Controller.
 */
class Model {

    companion object {
        private var entities: MutableList<Entity> = mutableListOf()

        fun clearEntities() {
            entities = mutableListOf()
        }

        fun setEntities(entities: MutableList<Entity>) {
            Model.entities = entities
        }

        fun getEntities(): MutableList<Entity> {
            return entities
        }
    }
}