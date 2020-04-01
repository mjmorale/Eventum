package ch.epfl.sdp.ui.event;

import android.content.Intent;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import ch.epfl.sdp.R;
import ch.epfl.sdp.ui.UIConstants;
import ch.epfl.sdp.utils.TestUtils;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class EventActivityTest {

    private static final String DUMMY_STRING = "test";

    @Rule
    public ActivityTestRule<EventActivity> mActivity = new ActivityTestRule<>(EventActivity.class, false, false);

    @Test
    public void EventActivity_LaunchesDefaultEventFragmentIfNoModeSpecified() {
        Intent intent = new Intent();
        intent.putExtra(UIConstants.BUNDLE_EVENT_REF, DUMMY_STRING);
        mActivity.launchActivity(intent);

        TestUtils.dismissSystemPopups(mActivity.getActivity());

        onView(withId(R.id.cardView_event)).check(matches(isDisplayed()));
    }
}
