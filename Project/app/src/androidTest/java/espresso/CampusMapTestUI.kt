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
class CampusMapTestUI {

    @get:Rule var activityScenarioRule = activityScenarioRule<StartupActivity>()

    @Test
    fun campusMapEntryExitWorks() {
        // Click on map button and check it's displayed
        onView(withId(R.id.map_fab)).perform(click())
        onView(withId(R.id.campus_map)).check(matches(isDisplayed()))

        // Then press back and check that we're back in the main activity
        Espresso.pressBack()
        onView(withId(R.id.ar_activity_layout)).check(matches(isDisplayed()))
    }
}