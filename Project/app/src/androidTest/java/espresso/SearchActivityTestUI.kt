package espresso

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.assertion.PositionAssertions.*
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.artourguideapp.R
import com.example.artourguideapp.StartupActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchActivityTestUI {

    @get:Rule var activityScenarioRule = activityScenarioRule<StartupActivity>()

    @Test
    fun searchForCommunicationsFacility() {
        // Click on map button and check it's displayed
        onView(withId(R.id.search_fab)).perform(click())

//        onView(withId(R.id.searchText)).perform(typeText("CF"))
//        Espresso.pressBack()
//
//        onView(withId(R.id.searchButton)).perform(click())

        onView(withText("Communications Facility (CF)")).perform(click())

        onView(withId(R.id.buildingDialog)).check(matches(isDisplayed()))

        Espresso.pressBack()

        Espresso.pressBack()
    }
}