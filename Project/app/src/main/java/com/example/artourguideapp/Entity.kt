package com.example.artourguideapp

import android.graphics.PointF
import android.location.Location

class Entity(
    private var name: String,
    private var id: Long,
    private var center: PointF,
    private var url: String,
    private var location : Location
) {


    fun getName(): String {
        return name
    }

    fun getID(): Long {
        return id
    }

    fun getCenter(): PointF {
        return center
    }

    fun getURL(): String {
        return url
    }

    fun setCenter(center: PointF) {
        this.center = center
    }

    fun getLocation() : Location {
        return location
    }

    fun setLocation(latitude : Double, longitude : Double) : Int
    {
        val latRange : ClosedRange<Double> = -90.0..90.0
        val lonRange : ClosedRange<Double> = -180.0..180.0
        if (latitude in latRange && longitude in lonRange)
        {
            location.latitude = latitude
            location.longitude = longitude
        }
        else
        {
            error("invalid latitude/longitude value in setLocation")
        }
        return 0
    }

    override fun toString(): String {
        return ("name: " + name
                + "\nid: " + id
                + "\ncenter: " + center
                + "\nurl: " + url
                + "\nlocation: " + location)
    }
}