package ch.epfl.sdp.ui.sharing;

import android.content.Intent;
import android.net.Uri;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sdp.R;
import ch.epfl.sdp.ui.auth.AuthActivity;
import ch.epfl.sdp.ui.main.MainActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class SharingTest {

    @Rule
    public ActivityTestRule<AuthActivity> mActivityRule = new ActivityTestRule<>(AuthActivity.class);

    @Before
    public void setup(){
        Intent intent = new Intent();
        intent.setData(Uri.parse("https://eventum.com/D6ONoAKeGRAtAGJ9hBC1/"));
        mActivityRule.launchActivity(intent);
    }
    @Test
    public void test(){
        //onView(withId(R.id.cardView_event)).check(matches(isDisplayed()));
    }
}
