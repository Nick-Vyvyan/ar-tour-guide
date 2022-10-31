package com.example.artourguideapp

import com.example.artourguideapp.entities.BuildingData
import com.example.artourguideapp.entities.LandmarkData

class Controller(private var server: String,
                 private var model: Model,
                 private var view: UserView,
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

    fun addLandmarks(landmarks: MutableList<LandmarkData>) {
        model.clearLandmarks()
        model.setLandmarks(landmarks)
    }

    fun getEntities(): MutableList<Entity> {
        return model.getEntities()
    }

    fun getBuildings(): MutableList<BuildingData> {
        return model.getBuildings()
    }

    fun getLandmarks(): MutableList<LandmarkData> {
        return model.getLandmarks()
    }

    fun updateView() {

    }
}