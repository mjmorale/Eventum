package ch.epfl.sdp;

import androidx.fragment.app.FragmentTransaction;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import ch.epfl.sdp.ui.event.EventFragment;
import ch.epfl.sdp.ui.event.EventViewModel;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class EventFragmentTest {
    private EventViewModel eventViewModel;
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testEventFragment() {
        String mockDescription = "This is really happening";
        String mockTitle = "Fake Event";
        Date mockDate = new Date(2010, 11, 10);

        mActivityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                EventFragment eventFragment = startEventFragment();
                eventFragment.getViewModel().getEvent().postValue(
                        new Event(mockTitle, mockDescription, mockDate)
                );

/*                onView(withId(R.id.description))
                        .check(matches(withText(mockDescription)));

                onView(withId(R.id.date))
                        .check(matches(withText(mockDate.toString())));

                onView(withId(R.id.title))
                        .check(matches(withText(mockTitle)));*/
            }
        });

    }

    private EventFragment startEventFragment() {
        MainActivity activity = (MainActivity) mActivityRule.getActivity();
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        EventFragment eventFragment = new EventFragment();
        transaction.add(eventFragment, "eventFragment");
        transaction.commitNow();
        return eventFragment;
    }

}
