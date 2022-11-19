package com.example.artourguideapp.cucumber.steps

import com.example.artourguideapp.entities.Entity
import com.example.artourguideapp.User
import com.example.artourguideapp.UserView
import cucumber.api.java.en.And
import cucumber.api.java.en.Given
import cucumber.api.java.en.Then
import cucumber.api.java.en.When

class RotatePhoneSteps {
    lateinit var user : User
    val camera = UserView()

    @Given("^Sean has already seen the buildings he is currently looking at$")
    fun camera_looking_at_building() {
        user.hasViewedAllNearbyBuildings()
    }
    @When("^he turns his phone away from its current location$")
    fun phone_is_rotating() {
        camera.relativeAngleHasChanged()
    }
    @And("^towards at least one other building$")
    fun at_least_one_nearby_building() {
        assert(camera.getAllNearestBuildings().size > 0)
    }
    @Then("^the tooltips and information for the new buildings should appear on his screen$")
    fun load_new_building_info() {
        camera.displayEntityTooltips(ArrayList<Entity>())
    }
}