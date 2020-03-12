package ch.epfl.sdp;

import androidx.test.espresso.action.ViewActions;
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
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class CreateEventFragmentTest {

    private Database db = new MockDatabase();
    private MockEvents mockEvents = new MockEvents();

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setup() {
        onView(withText("Create")).perform(click());
    }

    @Test
    public void testCreateEventFragment() {
        mActivityRule.getActivity().runOnUiThread(() -> {
            CreateEventFragment createEventFragment = startCreateEventFragment();
            createEventFragment.getViewModel().setDb(db);
        });

        Event event = mockEvents.getNextEvent();
        /* Try with incorrect values */
        onView(withHint(is("Title"))).perform(
                clearText(),
                typeText(""),
                closeSoftKeyboard());

        onView(withHint(is("Date"))).perform(
                clearText(),
                typeText(""),
                closeSoftKeyboard());

        onView(withId(R.id.createButton)).perform(
                click());

        /* Now try with correct values */
        mActivityRule.getActivity().runOnUiThread(() -> {
            CreateEventFragment createEventFragment = startCreateEventFragment();
            createEventFragment.getViewModel().setDb(db);
        });

        onView(withHint(is("Title"))).perform(
                clearText(),
                typeText(event.getTitle()),
                closeSoftKeyboard());

        onView(withHint(is("Description"))).perform(
                clearText(),
                typeText(event.getDescription()),
                closeSoftKeyboard());

        onView(withHint(is("Date"))).perform(
                clearText(),
                typeText("20/04/2020"),
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
