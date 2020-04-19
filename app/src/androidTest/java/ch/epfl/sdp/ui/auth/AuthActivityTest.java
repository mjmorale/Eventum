package ch.epfl.sdp.ui.auth;

import android.content.Intent;
import android.net.Uri;

import androidx.test.espresso.intent.Intents;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import ch.epfl.sdp.ui.UIConstants;
import ch.epfl.sdp.ui.event.EventActivity;
import ch.epfl.sdp.ui.sharing.Sharing;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
public class AuthActivityTest {

    private final static String DUMMY_USER_REF = "abcdefghijkl123";
    private final static String DUMMY_EVENT_REF = "dummyeventref987654321";

    @Rule
    public ActivityTestRule<AuthActivity> mActivity = new ActivityTestRule<>(AuthActivity.class, false, false);

    @Test
    public void AuthActivity_OnLoggedIn_LaunchesMainActivityWithEmptyBundle() {
        mActivity.launchActivity(new Intent());
        Intents.init();

        mActivity.getActivity().onLoggedIn(DUMMY_USER_REF);
        intended(allOf(
                hasComponent("ch.epfl.sdp.ui.main.MainActivity"),
                hasExtra(UIConstants.BUNDLE_USER_REF, DUMMY_USER_REF)
        ));

        Intents.release();
    }

    @Test
    public void AuthActivity_OnLoggedIn_LaunchesEventActivityWithSendActionIntent() {
        Intent sendAction = new Intent();
        sendAction.setAction(Intent.ACTION_SEND);
        sendAction.setDataAndTypeAndNormalize(Uri.parse(Sharing.DOMAIN_URL + "/" + DUMMY_EVENT_REF), "text/plain");

        mActivity.launchActivity(sendAction);
        Intents.init();

        mActivity.getActivity().onLoggedIn(DUMMY_USER_REF);
        intended(allOf(
                hasComponent("ch.epfl.sdp.ui.event.EventActivity"),
                hasExtra(UIConstants.BUNDLE_USER_REF, DUMMY_USER_REF),
                hasExtra(UIConstants.BUNDLE_EVENT_REF, DUMMY_EVENT_REF),
                hasExtra(UIConstants.BUNDLE_EVENT_MODE_REF, EventActivity.EventActivityMode.ATTENDEE)
        ));

        Intents.release();
    }
}
