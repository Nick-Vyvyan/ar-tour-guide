package com.example.artourguideapp

class Orientation {
    private var rotation = FloatArray(3)
    private var location = FloatArray(3)

    fun getRotation() : FloatArray {
        return rotation
    }

    fun getLocation() : FloatArray {
        return location
    }
}