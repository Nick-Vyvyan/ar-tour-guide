package com.example.artourguideapp

import android.graphics.PointF
import android.location.Location
import com.example.artourguideapp.entities.BuildingData
import com.example.artourguideapp.entities.BuildingEntity
import com.example.artourguideapp.entities.Entity
import com.example.artourguideapp.entities.LandmarkData
import com.example.artourguideapp.previouswork.Orientation
import com.example.artourguideapp.previouswork.UserView
import org.junit.Assert.*
import org.junit.Test

class GeneralUnitTests {
    /**
     * BuildingData Unit Tests
     */
    private val buildingData = BuildingData("","","","","","","","","")

    @Test
    fun getTitle() {
        assertTrue(buildingData.getTitle() == "")
    }

    @Test
    fun getTypes() {
        assertTrue(buildingData.getTypes() == "")
    }

    @Test
    fun getDepartments() {
        assertTrue(buildingData.getDepartments() == "")
    }

    @Test
    fun getAccessibilityInfo() {
        assertTrue(buildingData.getAccessibilityInfo() == "")
    }

    @Test
    fun getGenderNeutralRestrooms() {
        assertTrue(buildingData.getGenderNeutralRestrooms() == "")
    }

    @Test
    fun getComputerLabs() {
        assertTrue(buildingData.getComputerLabs() == "")
    }

    @Test
    fun getDataURL() {
        assertTrue(buildingData.getURL() == "")
    }

    /**
     * Controller Unit Tests
     */
    private val server: String = ""
    private val model = Model()
    private val view = UserView()
    private lateinit var user: User
    private val controller = Controller(server, model, view, user)

    @Test
    fun getBuildingData() {
        assertTrue(controller.getBuildings().isNotEmpty())
    }

    @Test
    fun getSculptureData() {
        assertTrue(controller.getLandmarks().isNotEmpty())
    }

    @Test
    fun updateView() {
        controller.updateView()
        assertTrue(view == UserView())
    }

    /**
     * BuildingEntity Unit Tests
     */
    private val buildingEntity = BuildingEntity("Wilson Library", PointF(), Location(""),
        BuildingData("Wilson Library", "", "", "", "",
            "", "", "", ""))

    @Test
    fun getName() {
        assertTrue(buildingEntity.getName() == "Wilson Library")
    }

    @Test
    fun getPerimeter() {
        assertTrue(buildingEntity.getCenter() == PointF())
    }

    @Test
    fun getEntityURL() {
        assertTrue(buildingEntity.getURL() == "")
    }

    @Test
    fun setPerimeter() {
        buildingEntity.setCenter(PointF())
        assertTrue(buildingEntity.getCenter() == PointF())
    }

    @Test
    fun setLocation() {
        val CFLat = 48.73266494618646
        val CFLon = -122.48524954354191
        assertTrue(buildingEntity.setLocation(CFLat, CFLon) == 0)
    }

    @Test
    fun getEntityLocation() {
        val CFLat = 48.73266494618646
        val CFLon = -122.48524954354191

        buildingEntity.setLocation(CFLat, CFLon)
        assertTrue(buildingEntity.getCentralLocation().latitude.equals(CFLat))
        assertTrue(buildingEntity.getCentralLocation().longitude.equals(CFLon))
    }

    /**
     * Model Unit Tests
     */
    @Test
    fun addEntity() {
        model.addEntity(buildingEntity)
        assertTrue(model.getEntitiesInView(user.getOrientation()) == ArrayList<Entity>())
    }

    @Test
    fun getEntitiesInView() {
        assertTrue(model.getEntitiesInView(user.getOrientation()) == ArrayList<Entity>())
    }

    /**
     * Orientation Unit Tests
     */
    private lateinit var orientation: Orientation

    @Test
    fun getOrientation() {
        assertTrue(orientation.getOrientation().equals(""))
    }

    /**
     * LandmarkData Unit Tests
     */
    private val landmarkData = LandmarkData("", "", "", "")

    @Test
    fun getLandmarkTitle(){
        assertTrue(landmarkData.getTitle() == "")
    }

    @Test
    fun getDescription(){
        assertTrue(landmarkData.getDescription() == "")
    }

    @Test
    fun getAudioDescription(){
        assertTrue(landmarkData.getAudioFileName() == "")
    }

    @Test
    fun getLandmarkURL(){
        assertTrue(landmarkData.getURL() == "")
    }

    /**
     * User Unit Tests
     */
    @Test
    fun getHeading(){
        assertTrue(user.getOrientation() == orientation)
    }

    /**
     * View Unit Tests
     */
    @Test
    fun displayBuildingData() {
        assertTrue(false)
    }

    @Test
    fun displaySculptureData() {
        assertTrue(false)
    }

    @Test
    fun displayEntityTooltips() {
        assertTrue(false)
    }

    @Test
    fun entityPressed() {
        assertTrue(false)
    }

    @Test
    fun getCurrentScrollPosition(){
        assertTrue(false)
    }

    @Test
    fun update() {
        assertTrue(false)
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