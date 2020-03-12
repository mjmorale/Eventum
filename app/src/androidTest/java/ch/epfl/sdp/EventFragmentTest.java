package ch.epfl.sdp;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.ui.event.EventFragment;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class EventFragmentTest {

    private Database db = new MockDatabase();
    private MockEvents mockEvents = new MockEvents();

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setup() {
        TestUtils.dismissSystemPopups(mActivityRule.getActivity());
        onView(withText("Event"))
                .inRoot(withDecorView(is(mActivityRule.getActivity().getWindow().getDecorView())))
                .perform(click());
    }

    @Test
    public void testEventFragment() {
        mActivityRule.getActivity().runOnUiThread(() -> {
            EventFragment eventFragment = startEventFragment();
            eventFragment.getViewModel().setDb(db);
        });

/*        onView(withId(R.id.description))
                .check(matches(withText(mockEvents.getNextEvent().getDescription())));

        onView(withId(R.id.date))
                .check(matches(withText(mockEvents.getNextEvent().getDate().toString())));

        onView(withId(R.id.title))
                .check(matches(withText(mockEvents.getNextEvent().getTitle())));*/

    }

    private EventFragment startEventFragment() {
        EventFragment eventFragment = EventFragment.newInstance("fake");
        mActivityRule.getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, eventFragment)
                .commitNow();
        return eventFragment;
    }

}
