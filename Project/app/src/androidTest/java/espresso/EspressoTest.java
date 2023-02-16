package espresso;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.assertion.PositionAssertions.isCompletelyLeftOf;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.artourguideapp.ArActivity;
import com.example.artourguideapp.R;
import com.example.artourguideapp.StartupActivity;

import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EspressoTest {

    @Rule
    public ActivityScenarioRule<StartupActivity> activityRule = new ActivityScenarioRule<>(StartupActivity.class);

    @Test
    public void loadingTextIsShown() {
        onView(withText("Loading...")).check(matches(isDisplayed()));
    }

    @Test
    public void mapButtonIsDisplayedLeftOfSearch() {

        onView(withId(R.id.map_fab)).check(matches(isDisplayed()));
        onView(withId(R.id.map_fab)).check(isCompletelyLeftOf(withId(R.id.search_fab)));
    }

}
