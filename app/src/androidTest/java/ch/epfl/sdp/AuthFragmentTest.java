package ch.epfl.sdp;

import android.content.Intent;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class AuthFragmentTest {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setup() {
        // Dismiss any system dialog that could hijack the focus
        // See: https://stackoverflow.com/questions/39457305/android-testing-waited-for-the-root-of-the-view-hierarchy-to-have-window-focus
        mActivityRule.getActivity().sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        onView(withText("Login"))
                .inRoot(withDecorView(is(mActivityRule.getActivity().getWindow().getDecorView())))
                .perform(click());
    }

    @Test
    public void authFragment_Create() {

    }
}
