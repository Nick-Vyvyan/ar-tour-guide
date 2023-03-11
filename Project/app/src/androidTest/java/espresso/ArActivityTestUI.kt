package espresso

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
class ArActivityTestUI {

    @get:Rule var activityScenarioRule = activityScenarioRule<StartupActivity>()

    @Test
    fun arActivityLayoutDisplayedCorrectly() {
        onView(withId(R.id.ar_activity_layout)).check(matches(isDisplayed()))

        // Check all buttons at bottom
        onView(withId(R.id.map_fab)).check(matches(isDisplayed()))
        onView(withId(R.id.start_tour_fab)).check(matches(isDisplayed()))
        onView(withId(R.id.search_fab)).check(matches(isDisplayed()))

        onView(withId(R.id.map_fab)).check(isCompletelyLeftOf(withId(R.id.start_tour_fab)))
        onView(withId(R.id.start_tour_fab)).check(isCompletelyLeftOf(withId(R.id.search_fab)))
    }
}