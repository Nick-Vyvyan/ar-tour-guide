package com.example.artourguideapp

class View {
    private var UIData: String = ""

    fun displayBuildingData(data: BuildingData) {

    }

    fun displaySculptureData(data: SculptureData) {

    }

    fun displayEntityTooltips(entity: ArrayList<Entity>) {

    }

    fun entityPressed() {

    }

    fun getCurrentScrollPosition(): Int {
        return 0;
    }

    fun turnOn() {

    }

    fun menuDisplayed(): Boolean {
        return false
    }

    fun sculptureAudioButtonClicked(): Boolean {
        return false
    }

    fun isAudioRecordingPlaying(): Boolean {
        return false
    }

    fun update() {

    }

    fun toolTipsDisplayed(): Int {
        return 3
    }

    fun positionTooltips() {

    }

    fun relativeAngleHasChanged() {
        TODO("Not yet implemented")
    }

    fun getAllNearestBuildings() : ArrayList<Entity> {
        return ArrayList<Entity>()
    }
}