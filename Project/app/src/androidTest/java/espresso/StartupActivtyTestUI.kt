package espresso

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.assertion.PositionAssertions.*
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.artourguideapp.Model
import com.example.artourguideapp.R
import com.example.artourguideapp.StartupActivity
import com.google.common.truth.Truth
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StartupActivtyTestUI {

    @get:Rule var activityScenarioRule = activityScenarioRule<StartupActivity>()

    @Test
    fun modelIsNotEmptyAfterStartup() {
        onView(withId(R.id.search_fab))

        val result = Model.getEntities().size > 0
        Truth.assertThat(result).isTrue()
    }
}