package ch.epfl.sdp;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class AuthActivityTest {

    @Rule
    public final ActivityTestRule<AuthActivity> mActivityRule = new ActivityTestRule<>(AuthActivity.class);

    @Test
    public void authActicity_LogoutButtonIsDisabledIfNotLoggedIn() {
        onView(withId(R.id.btn_logout)).check(matches(not(isEnabled())));
    }

    @Test
    public void authActicity_GoogleSignInButtonIsEnabledIfNotLoggedIn() {
        onView(withId(R.id.btn_google_sign_in)).check(matches(isEnabled()));
    }
}
