package ch.epfl.sdp.ui.settings;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import androidx.test.espresso.intent.Intents;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
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

        Intents.init();

        intending(hasComponent("ch.epfl.sdp.ui.auth.AuthActivity"))
                .respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, new Intent()));

        verify(mAuthenticator).logout();

        Intents.release();
    }
}
