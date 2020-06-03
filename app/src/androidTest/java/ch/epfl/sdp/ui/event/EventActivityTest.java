package ch.epfl.sdp.ui.event;

import android.app.Activity;
import android.content.Intent;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class EventActivityTest {

    private static final String DUMMY_STRING = "test";

    @Rule
    public ActivityTestRule<EventActivity> mActivity = new ActivityTestRule<>(EventActivity.class, false, false);

    @Test
    public void EventActivity_ClosesIfNoEventRefIsSpecified() {
        Intent intent = new Intent();
        mActivity.launchActivity(intent);

        assertEquals(Activity.RESULT_CANCELED, mActivity.getActivityResult().getResultCode());
    }
}
