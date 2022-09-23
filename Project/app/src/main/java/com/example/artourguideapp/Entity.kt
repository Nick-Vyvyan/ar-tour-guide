package com.example.artourguideapp

import android.graphics.Point

class Entity(
    private var name: String,
    private var id: Long,
    private var perimeter: ArrayList<Point>,
    private var url: String
) {

    fun getName(): String {
        return name
    }

    fun getID(): Long {
        return id
    }

    fun getPerimeter(): ArrayList<Point> {
        return perimeter
    }

    fun getURL(): String {
        return url
    }

    fun setPerimeter(points: ArrayList<Point>) {
        perimeter = points
    }
}