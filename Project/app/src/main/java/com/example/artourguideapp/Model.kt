package com.example.artourguideapp

import com.example.artourguideapp.entities.*

/**
 * A class that stores structure information passed from a specific view class through a Controller.
 */
class Model {

    companion object {
        private var entities: MutableList<Entity> = mutableListOf()
        private var buildings: MutableList<BuildingEntity> = mutableListOf()
        private var landmarks: MutableList<LandmarkEntity> = mutableListOf()

        fun clearEntities() {
            entities = mutableListOf()
        }

        fun setEntities(entities: MutableList<Entity>) {
            Model.entities = entities
        }

        fun getEntities(): MutableList<Entity> {
            return entities
        }

        fun getBuildings(): MutableList<BuildingEntity> {
            return buildings
        }

        fun getLandmarks(): MutableList<LandmarkEntity> {
            return landmarks
        }
    }

//    constructor() : this(mutableListOf<Entity>())
//
//    constructor(_entities: MutableList<Entity>) {
//        entities = _entities
//    }

//    fun clearEntities() {
//        entities = mutableListOf()
//    }
//
//    fun setEntities(entities: MutableList<Entity>) {
//        Model.entities = entities
//    }
//
//    fun getEntities(): MutableList<Entity> {
//        return entities
//    }
//
//    fun getBuildings(): MutableList<BuildingEntity> {
//        return buildings
//    }
//
//    fun getLandmarks(): MutableList<LandmarkEntity> {
//        return landmarks
//    }
//
//    fun addEntity(entity: Entity) {
//        entities.add(entity)
//    }
//
//    fun getEntitiesInView(userOrientation: Orientation): MutableList<Entity> {
//        return entities
//    }
}