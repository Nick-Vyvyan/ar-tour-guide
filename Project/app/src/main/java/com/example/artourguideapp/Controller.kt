package com.example.artourguideapp

import com.example.artourguideapp.entities.*

/**
 * A class that passes information to the Model class from a specific view class, or vice-versa.
 */
class Controller()
{
    fun setEntities(entities: MutableList<Entity>) {
        Model.clearEntities()
        Model.setEntities(entities)
    }

    fun getEntities(): MutableList<Entity> {
        return Model.getEntities()
    }

    fun getBuildings(): MutableList<BuildingEntity> {
        return Model.getBuildings()
    }

    fun getLandmarks(): MutableList<LandmarkEntity> {
        return Model.getLandmarks()
    }

    fun updateView() {

    }
}