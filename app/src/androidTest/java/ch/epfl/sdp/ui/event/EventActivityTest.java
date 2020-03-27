package ch.epfl.sdp.ui.event;

import android.app.Activity;
import android.content.Intent;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import ch.epfl.sdp.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class EventActivityTest {

    private static final String DUMMY_STRING = "test";

    @Rule
    public ActivityTestRule<EventActivity> mActivity = new ActivityTestRule<>(EventActivity.class, false, false);

    @Test
    public void EventActivity_FailsToLaunchWithNullEventRef() {
        Intent intent = new Intent();
        intent.putExtra(EventActivity.EVENT_MODE_EXTRA, EventActivity.EventActivityMode.ORGANIZER);
        mActivity.launchActivity(intent);

        assertEquals(Activity.RESULT_CANCELED, mActivity.getActivityResult().getResultCode());
    }

    @Test
    public void EventActivity_LaunchesDefaultEventFragmentIfNoModeSpecified() {
        Intent intent = new Intent();
        intent.putExtra(EventActivity.EVENT_REF_EXTRA, DUMMY_STRING);
        mActivity.launchActivity(intent);

        onView(withId(R.id.cardView_event)).check(matches(isDisplayed()));
    }
}
