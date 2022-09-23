package com.example.artourguideapp.cucumber.steps

import android.graphics.Point
import com.example.artourguideapp.*
import cucumber.api.java.en.Given
import cucumber.api.java.en.Then
import cucumber.api.java.en.When

class AudioRecordingSteps {
    val model = Model()
    val view = View()
    val user = User()
    val controller = Controller("", model, view, user)
    var sculpture = Entity("", 0, ArrayList<Point>(), "")
    var sculptureData = SculptureData("","","","")

    @Given("^Sarah has brought up a menu for a specific building$")
    fun camera_looking_at_building() {
        sculpture = model.getEntitiesInView(user.getOrientation()).get(0)
        assert(!sculpture.getName().equals(""))
        assert(view.menuDisplayed())
    }
    @When("^she clicks the Anecdote button in the Audio section$")
    fun click_audio_button() {
        assert(view.sculptureAudioButtonClicked())
    }
    @Then("^an audio recording of the building information should play$")
    fun play_audio_recording() {
        assert(view.isAudioRecordingPlaying())
    }
}