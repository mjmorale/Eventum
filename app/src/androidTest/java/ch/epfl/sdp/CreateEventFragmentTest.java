package ch.epfl.sdp;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.ui.event.CreateEventFragment;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class CreateEventFragmentTest {

    private Database mDb = new MockDatabase();
    private MockEvents mMockEvents = new MockEvents();
    private final static String TITLE = "title";
    private final static String DESCRIPTION = "Description";
    private final static String DATE = "date";

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setup() {
        onView(withText("Create"))
                .inRoot(withDecorView(is(mActivityRule.getActivity().getWindow().getDecorView())))
                .perform(click());
    }

    @Test
    public void testCreateEventFragment() {
        /* Now try with correct values */
        mActivityRule.getActivity().runOnUiThread(() -> {
            CreateEventFragment createEventFragment = startCreateEventFragment();
            createEventFragment.getViewModel().setDb(mDb);
        });

        Event event = mMockEvents.getNextEvent();
        onView(withHint(is(TITLE))).perform(
                clearText(),
                typeText(event.getTitle()),
                closeSoftKeyboard());

        onView(withHint(is(DESCRIPTION))).perform(
                clearText(),
                typeText(event.getDescription()),
                closeSoftKeyboard());

        onView(withHint(is(DATE))).perform(
                clearText(),
                typeText("20/04/2020"),
                closeSoftKeyboard());

        onView(withId(R.id.createButton)).perform(
                click());
    }

    @Test
    public void testCreateIncorrectEventFragment() {
        mActivityRule.getActivity().runOnUiThread(() -> {
            CreateEventFragment createEventFragment = startCreateEventFragment();
            createEventFragment.getViewModel().setDb(mDb);
        });

        /* Try with incorrect values */
        onView(withHint(is(TITLE))).perform(
                clearText(),
                typeText(""),
                closeSoftKeyboard());

        onView(withHint(is(DATE))).perform(
                clearText(),
                typeText(""),
                closeSoftKeyboard());

        onView(withId(R.id.createButton)).perform(
                click());
    }

    private CreateEventFragment startCreateEventFragment() {
        CreateEventFragment createEventFragment = new CreateEventFragment();
        mActivityRule.getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, createEventFragment)
                .commitNow();
        return createEventFragment;
    }
}
