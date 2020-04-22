package ch.epfl.sdp.ui.settings;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class HeaderFragmentTest extends SettingsFragmentTest {

    @Before
    public void setup() {
        super.setup();
    }

    @Test
    public void HeaderFragment_DisplayCorrectUserInfo() throws Throwable {
        mActivity.runOnUiThread(() -> {
            mUserLiveData.setValue(DUMMY_USER);
        });

        onView(withText(DUMMY_USER.getEmail())).check(matches(isDisplayed()));
    }

    @Test
    public void HeaderFragment_LogoutCallsAuthenticator() {
        onView(withText("Log out")).perform(click());

        verify(mAuthenticator).logout();
    }
}
