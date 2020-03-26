package ch.epfl.sdp;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import ch.epfl.sdp.ui.main.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class SwipeTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void setup(){
        TestUtils.dismissSystemPopups(mActivityRule.getActivity());
        onView(withText("Swipe"))
                .inRoot(withDecorView(is(mActivityRule.getActivity().getWindow().getDecorView())))
                .perform(click());
    }

    @Test
    public void buttonToSwipeFragment() {
        onView(withId(R.id.frame)).check(matches((isDisplayed())));
    }

    @Test
    public void scrollShowDeny() {
        onView(withId(R.id.frame)).perform(swipeLeft());
        onView(withId(R.id.frame)).check(matches((isDisplayed())));

        //---keep it just in case
        //onView(withId(R.id.deny_indicator)).check(matches((withAlpha(1))));
        //onView(allOf(withId(R.id.deny_indicator), isDisplayed())).check(matches((withAlpha(1))));
        //SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) getView().findViewById(R.id.frame);
        //View view = flingContainer.getSelectedView();
        //view.check
    }

    @Test
    public void scrollShowAccept() {
        onView(withId(R.id.frame)).perform(swipeRight());
        onView(withId(R.id.frame)).check(matches((isDisplayed())));
    }

    @Test
    public void clickSwapsToDetailled(){
        onView(withId(R.id.frame)).perform(click());
        onView(withId(R.id.cardView_event)).check(matches(isDisplayed()));
    }

    @Test
    public void clickSwapsToDetailledAndBack(){
        onView(withId(R.id.frame)).perform(click());
        onView(withId(R.id.cardView_event)).check(matches(isDisplayed()));
        onView(withId(R.id.back_button)).perform(click());
        onView(withId(R.id.frame)).check(matches(isDisplayed()));
    }
}

