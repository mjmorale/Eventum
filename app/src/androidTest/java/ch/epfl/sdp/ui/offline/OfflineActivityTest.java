package ch.epfl.sdp.ui.offline;

import android.content.Intent;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ch.epfl.sdp.R;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.auth.UserInfo;
import ch.epfl.sdp.ui.ServiceProvider;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class OfflineActivityTest {

    private static final String DUMMY_USERREF = "sdfkjghsdflkjghsdlfkgjh";
    private static final UserInfo DUMMY_USERINFO = new UserInfo(DUMMY_USERREF, "testname", "testemail");

    @Mock
    private Authenticator mAuthenticator;

    @Rule
    public ActivityTestRule<OfflineActivity> mActivity = new ActivityTestRule<>(OfflineActivity.class, false, false);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void OfflineActivity_CheckToolbarIsDisplayed() {
        when(mAuthenticator.getCurrentUser()).thenReturn(DUMMY_USERINFO);
        ServiceProvider.getInstance().setAuthenticator(mAuthenticator);

        mActivity.launchActivity(new Intent());

        onView(withId(R.id.main_toolbar)).check(matches(isDisplayed()));
    }
}
