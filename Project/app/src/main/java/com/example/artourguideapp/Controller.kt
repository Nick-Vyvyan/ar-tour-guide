package com.example.artourguideapp

class Controller(private var server: String,
                 private var model: Model,
                 private var view: View,
                 private var user: User?)
{
    fun addEntities(entities: MutableList<Entity>) {
        model.clearEntities()
        model.setEntities(entities)
    }

    fun addBuildings(buildings: MutableList<BuildingData>) {
        model.clearBuildings()
        model.setBuildings(buildings)
    }

    fun addLandmarks(landmarks: MutableList<SculptureData>) {
        model.clearLandmarks()
        model.setLandmarks(landmarks)
    }

    fun getEntities(): MutableList<Entity> {
        return model.getEntities()
    }

    fun getBuildings(): MutableList<BuildingData> {
        return model.getBuildings()
    }

    fun getLandmarks(): MutableList<SculptureData> {
        return model.getLandmarks()
    }

    fun updateView() {

    }
}