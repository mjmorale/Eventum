package ch.epfl.sdp.ui.auth;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import androidx.test.uiautomator.UiDevice;
import ch.epfl.sdp.R;
import ch.epfl.sdp.ui.auth.AuthActivity;

public class AuthActivityEspressoTest {
    @Rule
    public ActivityTestRule<AuthActivity> mActivityTestRule = new ActivityTestRule<>(AuthActivity.class);


    @Test
    public void AuthActivity_test_button() {
        onView(ViewMatchers.withId(R.id.btn_google_sign_in)).perform(click());
        UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mDevice.pressBack();
    }
}
