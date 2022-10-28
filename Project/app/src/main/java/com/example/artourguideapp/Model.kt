package com.example.artourguideapp

class Model {
    private var entities: ArrayList<Entity>
    private lateinit var buildings: MutableList<BuildingData>
    private lateinit var landmarks: MutableList<SculptureData>

    constructor() : this(ArrayList<Entity>())

    constructor(_entities: ArrayList<Entity>) {
        entities = _entities
    }

    fun clearBuildings() {
        buildings = mutableListOf()
    }

    fun clearLandmarks() {
        landmarks = mutableListOf()
    }

    fun setBuildings(buildings: MutableList<BuildingData>) {
        this.buildings = buildings
    }

    fun setLandmarks(landmarks: MutableList<SculptureData>) {
        this.landmarks = landmarks
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

    fun getEntitiesInView(userOrientation: Orientation): ArrayList<Entity> {
        return entities
    }
}