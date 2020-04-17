package ch.epfl.sdp.ui.sharing;

import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import ch.epfl.sdp.User;
import ch.epfl.sdp.auth.Authenticator;
import java.util.Arrays;
import ch.epfl.sdp.R;
import ch.epfl.sdp.mocks.MockFragmentFactory;
import ch.epfl.sdp.ui.auth.AuthActivity;
import ch.epfl.sdp.ui.auth.AuthFragment;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SharingTest {



    @Mock
    private Authenticator mAuthenticatorMock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }


    @SuppressWarnings("unchecked")
    @Test
    public void MyFragment() {

        when(mAuthenticatorMock.getCurrentUser()).thenReturn(new User("Uid","Name", "Email"));

        FragmentScenario<AuthFragment> scenario = FragmentScenario.launchInContainer(
                AuthFragment.class,
                new Bundle(),
                R.style.Theme_AppCompat,
                new MockFragmentFactory(AuthFragment.class, mAuthenticatorMock,Uri.parse("https://eventum.com/D6ONoAKeGRAtAGJ9hBC1/"))
        );
        onView(withId(R.id.imageView)).check(matches((isDisplayed())));
    }

    @Test
    public void sharing(){
        Sharing sharing = new Sharing(Arrays.asList("anyRef"));
        assertTrue(sharing.getShareIntent()!=null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void sharing_null_argument(){
        Sharing sharing = new Sharing(null);
        sharing.getShareIntent();
    }

    @Test
    public void sharingBuilder(){
        SharingBuilder sharingBuilder = new SharingBuilder();
        sharingBuilder.setRef("anyRef");
        assertTrue(sharingBuilder.build()!=null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void sharingBuilder_null_argument(){
        SharingBuilder sharingBuilder = new SharingBuilder();
        sharingBuilder.build();
    }
}
