package com.example.artourguideapp

import android.graphics.Point
import org.junit.Test

import org.junit.Assert.*
import java.io.File

/**
 * Unit tests for User Story P0-1.
 *
 * (As a prospective student, so that I can learn more about campus,
 * I want to access information about buildings surrounding my location.)
 */
class P0_1_UnitTests {

    /**
     * BuildingData Unit Tests
     */

    val buildingData = BuildingData("","","","","","","","","")

    @Test
    fun getTitle() {
        assertTrue(!buildingData.getTitle().equals(""))
    }

    @Test
    fun getCode() {
        assertTrue(!buildingData.getCode().equals(""))
    }

    @Test
    fun getTypes() {
        assertTrue(!buildingData.getTypes().equals(""))
    }

    @Test
    fun getDepartments() {
        assertTrue(!buildingData.getDepartments().equals(""))
    }

    @Test
    fun getAccessibilityInfo() {
        assertTrue(!buildingData.getAccessibilityInfo().equals(""))
    }

    @Test
    fun getGenderNeutralRestrooms() {
        assertTrue(!buildingData.getGenderNeutralRestrooms().equals(""))
    }

    @Test
    fun getComputerLabs() {
        assertTrue(!buildingData.getComputerLabs().equals(""))
    }

    @Test
    fun getParkingInfo() {
        assertTrue(!buildingData.getParkingInfo().equals(""))
    }

    @Test
    fun getDataURL() {
        assertTrue(!buildingData.getURL().equals(""))
    }

    /**
     * Controller Unit Tests
     */
    val server: String = ""
    val model = Model()
    val view = View()
    val user = User()
    val controller = Controller(server, model, view, user)

    @Test
    fun getBuildingData() {
        assertTrue(controller.getBuildingData("").equals(""))
    }

    @Test
    fun getSculptureData() {
        assertTrue(controller.getSculptureData("").equals(""))
    }

    @Test
    fun updateView() {
        controller.updateView()
        assertTrue(view.equals(View()))
    }

    /**
     * Entity Unit Tests
     */
    val entity = Entity("",0,ArrayList<Point>(),"")


    @Test
    fun getName() {
        assertTrue(entity.getName().equals("Wilson Library"))
    }

    @Test
    fun getID() {
        assertTrue(entity.getID().equals(14))
    }

    @Test
    fun getPerimeter() {
        assertTrue(entity.getPerimeter().equals(ArrayList<Point>()))
    }

    @Test
    fun getEntityURL() {
        assertTrue(entity.getName().equals(""))
    }

    @Test
    fun setPerimeter() {
        val newPoints = ArrayList<Point>()
        entity.setPerimeter(newPoints)
        assertTrue(entity.getPerimeter().equals(ArrayList<Point>()))
    }

    @Test
    fun setLocation() {
        val CFLat = 48.73266494618646
        val CFLon = -122.48524954354191
        assertTrue(entity.setLocation(CFLat, CFLon).equals(0))
    }

    @Test
    fun getEntityLocation() {
        val CFLat : Double = 48.73266494618646
        val CFLon : Double = -122.48524954354191

        entity.setLocation(CFLat, CFLon)
        assertTrue(entity.getLocation().latitude.equals(CFLat))
        assertTrue(entity.getLocation().longitude.equals(CFLon))
    }

    /**
     * Model Unit Tests
     */
    @Test
    fun addEntity() {
        model.addEntity(Entity("",0, ArrayList<Point>(), ""))
        assertTrue(model.getEntitiesInView(user.getOrientation()).equals(ArrayList<Entity>()))
    }

    @Test
    fun getEntitiesInView() {
        assertTrue(model.getEntitiesInView(user.getOrientation()).equals(ArrayList<Entity>()))
    }

    /**
     * Orientation Unit Tests
     */
    val orientation = Orientation()
    @Test
    fun getRotation() {
        assertTrue(orientation.getRotation().equals(""))
    }

    @Test
    fun getLocation() {
        assertTrue(orientation.getLocation().equals(""))
    }

    /**
     * SculptureData Unit Tests
     */
    val sculptureData = SculptureData("A", "B", "", "C")
    @Test
    fun getSculptTitle(){
        assertTrue(sculptureData.getTitle().equals(""))
    }

    @Test
    fun getDescription(){
        assertTrue(sculptureData.getDescription().equals(""))
    }

    @Test
    fun getAudioDescription(){
        assertTrue(sculptureData.getAudioDescription().equals(""))
    }

    @Test
    fun getSculptURL(){
        assertTrue(sculptureData.getURL().equals(""))
    }

    /**
     * User Unit Tests
     */
    @Test
    fun getOrientation(){
        assertTrue(user.getOrientation().equals(Orientation()))
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