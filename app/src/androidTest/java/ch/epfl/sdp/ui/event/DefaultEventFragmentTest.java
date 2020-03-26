package ch.epfl.sdp.ui.event;

import android.os.Bundle;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.runner.AndroidJUnit4;
import ch.epfl.sdp.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class DefaultEventFragmentTest {

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void DefaultEventFragment_DisplaysErrorMessageWithNullBundle() {
        FragmentScenario.launchInContainer(DefaultEventFragment.class, null, R.style.Theme_AppCompat, null);

        onView(withId(R.id.default_event_error_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.default_event_layout)).check(matches(not(isDisplayed())));
    }

    @Test
    public void DefaultEventFragment_DisplaysErrorMessageWithEmptyBundle() {
        FragmentScenario.launchInContainer(DefaultEventFragment.class, new Bundle(), R.style.Theme_AppCompat, null);

        onView(withId(R.id.default_event_error_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.default_event_layout)).check(matches(not(isDisplayed())));
    }
}
