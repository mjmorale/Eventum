package ch.epfl.sdp;

import androidx.test.espresso.contrib.PickerActions;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;

import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.ui.event.CreateEventFragment;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static java.lang.Thread.sleep;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class CreateEventFragmentTest {

    private Database mDb = new MockDatabase();
    private static SimpleDateFormat mFormatter = new SimpleDateFormat("dd/MM/yyyy");

    private static final Event mMockEvent = MockEvents.getCurrentEvent();
    private static final String DATE = mFormatter.format(mMockEvent.getDate());
    private static final String TITLE = mMockEvent.getTitle();
    private static final String DESCRIPTION = mMockEvent.getDescription();
    private static final String ADDRESS = mMockEvent.getAddress();
    private static final String EMPTY = "";
    private static final int DAY = mMockEvent.getDate().getDay();
    private static final int MONTH = mMockEvent.getDate().getMonth();
    private static final int YEAR = mMockEvent.getDate().getYear();

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
    public void testCreateEventFragment() throws InterruptedException {
        launchEventFragment();

        // Now try with correct values
        doCorrectInput();
        // Sleep to let the Geocoder found some locations
        sleep(5000);

        onView(withId(R.id.geo_search_result_text))
                .inRoot(isPlatformPopup())
                .check(matches(isDisplayed()))
                .perform(click());

       onView(withId(R.id.createButton))
                .perform(scrollTo(), click());

        // Check the created event page
        onView(withId(R.id.description))
                .check(matches(withText(DESCRIPTION)));

        onView(withId(R.id.date))
                .check(matches(withText(DATE)));

        onView(withId(R.id.title))
                .check(matches(withText(TITLE)));

        onView(withId(R.id.address))
                .check(matches(withText(ADDRESS)));

    }

    @Test
    public void testCreateIncorrectEventFragment() {
        launchEventFragment();

        // Try with incorrect values
        onView(withId(R.id.title)).perform(
                clearText(),
                typeText(EMPTY),
                closeSoftKeyboard());

        onView(withId(R.id.createButton)).perform(
                scrollTo(),
                click());
    }

    private void launchEventFragment() {
        mActivityRule.getActivity().runOnUiThread(() -> {
            CreateEventFragment createEventFragment = startCreateEventFragment();
            createEventFragment.getViewModel().setDb(mDb);
        });
    }

    private CreateEventFragment startCreateEventFragment() {
        CreateEventFragment createEventFragment = new CreateEventFragment(new MockDatabase());
        mActivityRule.getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, createEventFragment)
                .commitNow();
        return createEventFragment;
    }

    private void doCorrectInput() {
        onView(withId(R.id.title)).perform(
                typeText(TITLE),
                closeSoftKeyboard());

        onView(withId(R.id.description)).perform(
                typeText(DESCRIPTION),
                closeSoftKeyboard());

        onView(withId(R.id.date)).perform(
                PickerActions.setDate(YEAR, MONTH, DAY),
                closeSoftKeyboard());

        onView(withId(R.id.geo_autocomplete)).perform(
                scrollTo(),
                typeText("Lausanne"));
    }
}
