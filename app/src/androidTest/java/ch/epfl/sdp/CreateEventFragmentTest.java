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
import ch.epfl.sdp.ui.createevent.CreateEventActivity;
import ch.epfl.sdp.ui.createevent.CreateEventViewModel;
import ch.epfl.sdp.ui.main.MainActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class CreateEventFragmentTest {

    private static SimpleDateFormat mFormatter = new SimpleDateFormat("dd/MM/yyyy");

    private static final Event mMockEvent = MockEvents.getCurrentEvent();
    private static final String DATE = mFormatter.format(mMockEvent.getDate());
    private static final String TITLE = mMockEvent.getTitle();
    private static final String DESCRIPTION = mMockEvent.getDescription();
    private static final String EMPTY = "";
    private static final int DAY = 30;
    private static final int MONTH = 6;
    private static final int YEAR = 2017;

    @Rule
    public final ActivityTestRule<CreateEventActivity> mActivityRule =
            new ActivityTestRule<>(CreateEventActivity.class);

    @Test
    public void testCreateEventFragment() {
        // Now try with correct values
        onView(withId(R.id.title)).perform(
                clearText(),
                typeText(TITLE),
                closeSoftKeyboard());

        onView(withId(R.id.description)).perform(
                clearText(),
                typeText(DESCRIPTION),
                closeSoftKeyboard());

        onView(withId(R.id.date)).perform(
                PickerActions.setDate(YEAR, MONTH, DAY),
                closeSoftKeyboard());

        onView(withId(R.id.createButton))
                .perform(click());
    }

    @Test
    public void testCreateIncorrectEventFragment() {
        // Try with incorrect values
        onView(withHint(is("Title"))).perform(
                clearText(),
                typeText(EMPTY),
                closeSoftKeyboard());

        onView(withId(R.id.createButton)).perform(
                click());
    }
}
