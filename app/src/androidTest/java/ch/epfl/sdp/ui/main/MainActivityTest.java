package ch.epfl.sdp.ui.main;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import ch.epfl.sdp.utils.TestUtils;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private static final String DUMMY_STRING = "test";

    @Rule
    public ActivityTestRule<MainActivity> mActivity = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setup() {
        TestUtils.dismissSystemPopups(mActivity.getActivity());
    }

    @Test
    public void MainActivity_CreatingEventLaunchesEventActivity() {
        //TODO: Uncomment once migrated to CirrusCI
        /*Intents.init();

        Intent resultIntent = new Intent();
        resultIntent.putExtra(EventActivity.EVENT_MODE_EXTRA, EventActivity.EventActivityMode.ORGANIZER);
        resultIntent.putExtra(EventActivity.EVENT_REF_EXTRA, DUMMY_STRING);
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, resultIntent);

        intending(hasComponent("ch.epfl.sdp.ui.createevent.CreateEventActivity")).respondWith(result);

        onView(withId(R.id.main_actionbar_add)).perform(click());

        onView(withId(R.id.default_event_layout)).check(matches(isDisplayed()));

        Intents.release();*/
    }

    @Test
    public void MainActivity_FailingToCreateEventDisplaysAToast() {
        /*Intents.init();

        Intent resultIntent = new Intent();
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_CANCELED, resultIntent);

        intending(hasComponent("ch.epfl.sdp.ui.createevent.CreateEventActivity")).respondWith(result);

        onView(withId(R.id.main_actionbar_add)).perform(click());

        onView(withText("Failed to create event")).inRoot(withDecorView(not(mActivity.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));

        Intents.release();*/
    }

    @Test
    public void MainActivity_SettingsCanBeOpenedFromNavigationView() {
        /*Intents.init();

        onView(withId(R.id.main_drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        onView(withId(R.id.main_nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_settings));

        intended(hasComponent("ch.epfl.sdp.ui.settings.SettingsActivity"));

        Intents.release();*/
    }

    @Test
    public void MainActivity_UserSettingsCanBeOpenedFromNavigationView() {
        /*Intents.init();

        onView(withId(R.id.main_drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        onView(withId(R.id.main_nav_header_layout))
                .perform(click());

        intended(hasComponent("ch.epfl.sdp.ui.user.UserActivity"));

        Intents.release();*/
    }
}
