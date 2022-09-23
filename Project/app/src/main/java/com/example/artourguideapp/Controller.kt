package com.example.artourguideapp

import java.io.File

class Controller(private var server: String,
                 private var model: Model,
                 private var view: View,
                 private var user: User)
{

    fun getBuildingData(url: String): BuildingData {
        return BuildingData("","","","","","","","","")
    }

    fun getSculptureData(url: String): SculptureData {
        return SculptureData("", "", "","")
    }

    fun updateView() {

    }
}