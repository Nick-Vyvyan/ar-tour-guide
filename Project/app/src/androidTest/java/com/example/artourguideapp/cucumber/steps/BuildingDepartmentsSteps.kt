//package com.example.artourguideapp.cucumber.steps
//
//import android.graphics.PointF
//import android.location.Location
//import com.example.artourguideapp.*
//import com.example.artourguideapp.entities.BuildingData
//import com.example.artourguideapp.entities.BuildingEntity
//import cucumber.api.java.en.And
//import cucumber.api.java.en.Given
//import cucumber.api.java.en.Then
//import cucumber.api.java.en.When
//
//class BuildingDepartmentsSteps {
//    private val controller = Controller()
//    var building = BuildingEntity(Location("SculptureTest"),
//        BuildingData("", "", "", "", "", "", "", "", "", ""))
//
//    @Given("^Tom has found a building on campus$")
//    fun get_nearest_building() {
////        building = model.getEntitiesInView(user.getOrientation())[0] as BuildingEntity
////        assert(building.getName() != "")
//    }
//    @And("^he has brought up the menu for the building information$")
//    fun get_nearest_building_name() {
//        building = controller.getEntities()[0] as BuildingEntity
//    }
//    @When("^he reads through the content of the menu$")
//    fun how_much_menu_is_read() {
////        val x = view.getCurrentScrollPosition()
////        assert(x > 2)
//    }
//    @Then("^the departments inside the building should be listed$")
//    fun has_building_departments() {
//        assert((building.getEntityData() as BuildingData).getDepartments().isNotEmpty())
//    }
//}