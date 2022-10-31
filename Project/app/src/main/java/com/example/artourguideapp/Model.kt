package com.example.artourguideapp

class Model {
    private var entities: MutableList<Entity>
    private lateinit var buildings: MutableList<BuildingData>
    private lateinit var landmarks: MutableList<SculptureData>

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

    fun setBuildings(buildings: MutableList<BuildingData>) {
        this.buildings = buildings
    }

    fun setLandmarks(landmarks: MutableList<SculptureData>) {
        this.landmarks = landmarks
    }

    fun getEntities(): MutableList<Entity> {
        return entities
    }

    fun getBuildings(): MutableList<BuildingData> {
        return buildings
    }

    fun getLandmarks(): MutableList<SculptureData> {
        return landmarks
    }

    fun addEntity(entity: Entity) {
        entities.add(entity)
    }

    fun getEntitiesInView(userOrientation: Orientation): MutableList<Entity> {
        return entities
    }
}