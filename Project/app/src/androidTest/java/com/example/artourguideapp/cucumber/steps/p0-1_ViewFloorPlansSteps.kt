package com.example.artourguideapp.cucumber.steps

import cucumber.api.java.en.And
import cucumber.api.java.en.Given
import cucumber.api.java.en.Then
import cucumber.api.java.en.When

class ViewFloorPlansSteps {
    @Given("^Katie's smartphone camera is positioned to be looking at a building$")
    fun camera_looking_at_building() {
        camera.isLookingAtBuilding()
    }
    @And("^she sees the name of the building displayed on the screen$")
    fun get_nearest_building_name() {
        camera.nearestBuilding.getName()
    }
    @When("^she clicks on the tooltip of the building name$")
    fun click_tooltip() {
        camera.nearestBuilding.getTooltip().click()
    }
    @And("^clicks on the button in the menu that links to the building's floor plans$")
    fun click_floor_plans_button() {
        camera.nearestBuilding.getMenu().displayFloorPlans()
    }
    @Then("^the floor plans should be displayed on her screen$")
    fun floor_plans_displayed() {
        assert(camera.nearestBuilding.getMenu().areFloorPlansDisplayed() == true)
    }
}