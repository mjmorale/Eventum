package ch.epfl.sdp.ui.settings;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import ch.epfl.sdp.utils.TestUtils;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class SettingsActivityTest {

    @Rule
    public ActivityTestRule<SettingsActivity> mActivity = new ActivityTestRule<>(SettingsActivity.class);

    @Before
    public void setup() {
        TestUtils.dismissSystemPopups(mActivity.getActivity());
    }

    @Test
    public void SettingsActivity_HasAccountMenu() {
        onView(withText("Account")).check(matches(isDisplayed()));
    }

    @Test
    public void SettingsActivity_HasLogOutMenu() {
        onView(withText("Log out")).check(matches(isDisplayed()));
    }

    @Test
    public void SettingsActivity_AccountMenuHasAvatar() {
        onView(withText("Account")).perform(click());
        onView(withText("Avatar")).check(matches(isDisplayed()));
    }

    @Test
    public void SettingsActivity_AccountMenuHasName() {
        onView(withText("Account")).perform(click());
        onView(withText("Name")).check(matches(isDisplayed()));
    }

    @Test
    public void SettingsActivity_AccountMenuHasEMail() {
        onView(withText("Account")).perform(click());
        onView(withText("E-mail")).check(matches(isDisplayed()));
    }

    @Test
    public void SettingsActivity_AccountMenuHasDelete() {
        onView(withText("Account")).perform(click());
        onView(withText("Delete account")).check(matches(isDisplayed()));
    }
}
