package com.example.artourguideapp

import com.example.artourguideapp.entities.*

class Controller(private var server: String,
                 private var model: Model,
                 private var view: UserView,
                 private var user: User?)
{
    fun addEntities(entities: MutableList<Entity>) {
        model.clearEntities()
        model.setEntities(entities)
    }

    fun addBuildings(buildings: MutableList<BuildingEntity>) {
        model.clearBuildings()
        model.setBuildings(buildings)
    }

    fun addLandmarks(landmarks: MutableList<LandmarkEntity>) {
        model.clearLandmarks()
        model.setLandmarks(landmarks)
    }

    fun getEntities(): MutableList<Entity> {
        return model.getEntities()
    }

    fun getBuildings(): MutableList<BuildingEntity> {
        return model.getBuildings()
    }

    fun getLandmarks(): MutableList<LandmarkEntity> {
        return model.getLandmarks()
    }

    fun updateView() {

    }
}