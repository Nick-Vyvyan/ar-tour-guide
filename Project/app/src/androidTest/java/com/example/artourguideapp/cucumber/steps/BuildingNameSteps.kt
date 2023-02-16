//package com.example.artourguideapp.cucumber.steps
//
//import android.graphics.PointF
//import android.location.Location
//import com.example.artourguideapp.*
//import com.example.artourguideapp.entities.LandmarkData
//import com.example.artourguideapp.entities.LandmarkEntity
//import cucumber.api.java.en.And
//import cucumber.api.java.en.Given
//import cucumber.api.java.en.Then
//import cucumber.api.java.en.When
//
//class BuildingNameSteps {
//    val controller = Controller()
//    private var landmark = LandmarkEntity("", PointF(), Location("SculptureTest"),
//        LandmarkData("", "", "", ""))
//
//    @Given("^John is using his smartphone camera to view the WWU campus in the app$")
//    fun turn_on_camera() {
//        // camera.turnOn()
//    }
//    @And("^he is close to a building$")
//    fun determine_closest_buildings() {
//        // model.getEntitiesInView(user.getOrientation())
//    }
//    @When("^the building enters his camera's field of vision$")
//    fun get_nearest_buildings() {
//        // model.getEntitiesInView(user.getOrientation())
//    }
//    @Then("^the screen displays a tooltip with the name of the building$")
//    fun display_tooltip() {
////        camera.displayEntityTooltips(ArrayList())
////        assert(camera.toolTipsDisplayed() > 0)
//    }
//    @And("^positions it near the center of the building to be clicked$")
//    fun position_tooltip() {
////        camera.positionTooltips()
//    }
//}