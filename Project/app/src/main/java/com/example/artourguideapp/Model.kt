package com.example.artourguideapp

import com.example.artourguideapp.entities.*

/**
 * A class that stores structure information passed from a specific view class through a Controller.
 */
class Model {
    private var entities: MutableList<Entity>
    private var buildings: MutableList<BuildingEntity> = mutableListOf()
    private var landmarks: MutableList<LandmarkEntity> = mutableListOf()

    constructor() : this(mutableListOf<Entity>())

    constructor(_entities: MutableList<Entity>) {
        entities = _entities
    }

    fun clearEntities() {
        entities = mutableListOf()
    }

    fun clearBuildings() {
        buildings = mutableListOf()
    }

    fun clearLandmarks() {
        landmarks = mutableListOf()
    }

    fun setEntities(entities: MutableList<Entity>) {
        this.entities = entities
    }

    fun setBuildings(buildings: MutableList<BuildingEntity>) {
        this.buildings = buildings
    }

    fun setLandmarks(landmarks: MutableList<LandmarkEntity>) {
        this.landmarks = landmarks
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

    fun addEntity(entity: Entity) {
        entities.add(entity)
    }

    fun getEntitiesInView(userOrientation: Orientation): MutableList<Entity> {
        return entities
    }
}