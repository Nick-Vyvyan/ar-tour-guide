package com.example.artourguideapp.cucumber.steps

import android.graphics.PointF
import android.location.Location
import com.example.artourguideapp.*
import com.example.artourguideapp.entities.BuildingData
import com.example.artourguideapp.entities.BuildingEntity
import com.example.artourguideapp.entities.Entity
import cucumber.api.java.en.And
import cucumber.api.java.en.Given
import cucumber.api.java.en.Then
import cucumber.api.java.en.When

class BuildingDepartmentsSteps {
    private val model = Model()
    private val view = UserView()
    private lateinit var user : User
    private val controller = Controller("", model, view, user)
    var building = BuildingEntity("", PointF(), Location("SculptureTest"),
        BuildingData("", "", "", "", "", "", "", "", ""))

    @Given("^Tom has found a building on campus$")
    fun get_nearest_building() {
        building = model.getEntitiesInView(user.getOrientation())[0] as BuildingEntity
        assert(building.getName() != "")
    }
    @And("^he has brought up the menu for the building information$")
    fun get_nearest_building_name() {
        building = controller.getBuildings()[0]
    }
    @When("^he reads through the content of the menu$")
    fun how_much_menu_is_read() {
        val x = view.getCurrentScrollPosition()
        assert(x > 2)
    }
    @Then("^the departments inside the building should be listed$")
    fun has_building_departments() {
        assert((building.getEntityData() as BuildingData).getDepartments().isNotEmpty())
    }
}