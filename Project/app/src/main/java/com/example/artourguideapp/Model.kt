package com.example.artourguideapp

class Model {
    private var entities: ArrayList<Entity>

    constructor() : this(ArrayList<Entity>())

    constructor(_entities: ArrayList<Entity>) {
        entities = _entities
    }

    fun addEntity(entity: Entity) {
        entities.add(entity)
    }

    fun getEntitiesInView(userOrientation: Orientation): ArrayList<Entity> {
        return entities
    }
}