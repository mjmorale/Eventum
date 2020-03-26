package ch.epfl.sdp.ui.user;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class UserActivityTest {

    @Rule
    public ActivityTestRule<UserActivity> mActivity = new ActivityTestRule<>(UserActivity.class);

    @Test
    public void UserActivity_DisplayUserStatisticsAsDefault() {
        onView(withText("User statistics")).check(matches(isDisplayed()));
    }

    @Test
    public void UserActivity_SecondTabDisplaysMyEvents() {
        onView(withText("My events")).perform(click());
        onView(withText("User events")).check(matches(isDisplayed()));
    }
}
