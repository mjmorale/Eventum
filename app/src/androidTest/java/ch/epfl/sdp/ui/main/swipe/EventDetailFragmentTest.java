package ch.epfl.sdp.ui.main.swipe;

import android.content.Intent;


import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sdp.R;
import ch.epfl.sdp.ui.main.MainActivity;


import android.content.Context;
import android.net.Uri;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class EventDetailFragmentTest {
    private EventDetailFragment mEventDetailFragment;

    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<MainActivity>(MainActivity.class);
    @Before
    public void setup(){
        Intent intent = new Intent();
        intent.setData(Uri.parse("https://eventum.com/2131165377"));
        mActivityRule.launchActivity(intent);
    }
    @Test
    public void test(){
        onView(withId(R.id.cardView_event)).check(matches(isDisplayed()));
    }
}
