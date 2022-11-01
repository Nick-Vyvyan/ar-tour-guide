package com.example.artourguideapp.cucumber.steps

import android.graphics.Point
import android.location.Location
import com.example.artourguideapp.*
import com.example.artourguideapp.entities.BuildingData
import com.example.artourguideapp.entities.Entity
import cucumber.api.java.en.And
import cucumber.api.java.en.Given
import cucumber.api.java.en.Then
import cucumber.api.java.en.When

class BuildingDepartmentsSteps {
    val model = Model()
    val view = UserView()
    private lateinit var user : User
    val controller = Controller("", model, view, user)
    var building = Entity("", 0, ArrayList<Point>(), "", Location("BuildingTest"))
    var buildingData = BuildingData("","","","","","","","","")

    @Given("^Tom has found a building on campus$")
    fun get_nearest_building() {
        building = model.getEntitiesInView(user.getOrientation()).get(0)
        assert(!building.getName().equals(""))
    }
    @And("^he has brought up the menu for the building information$")
    fun get_nearest_building_name() {
        buildingData = controller.getBuildingData(building.getURL())
    }
    @When("^he reads through the content of the menu$")
    fun how_much_menu_is_read() {
        val x = view.getCurrentScrollPosition()
        assert(x > 2)
    }
    @Then("^the departments inside the building should be listed$")
    fun has_building_departments() {
        assert((buildingData.getDepartments()?.length ?: 0) > 0)
    }
}