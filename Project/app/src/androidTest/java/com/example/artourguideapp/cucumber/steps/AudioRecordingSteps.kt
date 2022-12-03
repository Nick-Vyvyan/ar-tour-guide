package com.example.artourguideapp.cucumber.steps

import android.graphics.PointF
import android.location.Location
import com.example.artourguideapp.*
import com.example.artourguideapp.entities.LandmarkData
import com.example.artourguideapp.entities.LandmarkEntity
import cucumber.api.java.en.Given
import cucumber.api.java.en.Then
import cucumber.api.java.en.When

class AudioRecordingSteps {
    private val model = Model()
    private val view = UserView()
    lateinit var user : User
    val controller = Controller("", model, view, user)
    private var landmark = LandmarkEntity("", PointF(), Location("SculptureTest"),
        LandmarkData("", "", "", ""))

    @Given("^Sarah has brought up a menu for a specific building$")
    fun camera_looking_at_building() {
        landmark = model.getEntitiesInView(user.getOrientation())[0] as LandmarkEntity
        assert(landmark.getName() != "")
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