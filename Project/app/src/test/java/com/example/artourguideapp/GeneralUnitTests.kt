package com.example.artourguideapp

import android.graphics.PointF
import android.location.Location
import com.example.artourguideapp.entities.BuildingData
import com.example.artourguideapp.entities.BuildingEntity
import com.example.artourguideapp.entities.LandmarkData
import com.example.artourguideapp.previouswork.Orientation
import org.junit.Assert.*
import org.junit.Test

class GeneralUnitTests {
    /**
     * Controller Unit Tests
     */
    private val controller = Controller()

    @Test
    fun controllerGetEntities() {
        assertTrue(controller.getEntities().size == 0)
    }

    @Test
    fun controllerSetEntities() {
        controller.setEntities(mutableListOf())
        assertTrue(controller.getEntities().size == 0)
    }

    /**
     * Model Unit Tests
     */
    private val model = Model()

    @Test
    fun modelGetEntities() {
        assertTrue(Model.getEntities().size == 0)
    }

    @Test
    fun modelClearEntities() {
        Model.clearEntities()
        assertTrue(Model.getEntities().size == 0)
    }

    @Test
    fun modelSetEntities() {
        Model.setEntities(mutableListOf())
        assertTrue(Model.getEntities().size == 0)
    }

    /**
     * End of Class Method Tests
     */

    /*
    // ensure that menu shows up when a prospective student clicks on a tooltip
    @Test
    fun userSeesMenuFromClickingOnTooltip() {
        val buildings = camera.getAllNearestBuildings()
        user.clickOn(buildings[0].getTooltip())

        assertTrue(buildings[0].getMenu() is Menu)
    }

    // ensure that building tooltip displays its name to the prospective student
    @Test
    fun buildingTooltipDisplaysName() {
        assertEquals(camera.nearestBuilding.getTooltip(), camera.nearestBuilding.getName())
    }

    // ensure that when audio button is clicked on in building menu, recording successfully
    // plays for the prospective student
    @Test
    fun audioRecordingSuccessfullyPlays() {
        camera.nearestBuilding.getMenu().clickAudioButton()
        assertTrue(camera.nearestBuilding.getMenu().isPlayingAudio())
    }

    // ensure that recent search is added after prospective student searches for building
    @Test
    fun recentSearchIsAdded() {
        val searchString = "Miller Hall"

        view.searchBar.search(searchString)
        assertTrue(user.recentSearches.contains(searchString))
    }

    // ensure that new buildings are correctly loaded in upon rotation of the camera
    @Test
    fun newBuildingsLoadedInUponCameraRotation(){
        camera.changeRelativeAngleByDegrees(180)
        assertEquals(camera.buildingsCurrentlyLoadedIn().length, camera.getAllNearestBuildings().length)
    }

    */
}