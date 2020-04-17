package ch.epfl.sdp.ui.sharing;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.testing.FragmentScenario;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import ch.epfl.sdp.auth.Authenticator;
import java.util.Arrays;

import ch.epfl.sdp.R;
import ch.epfl.sdp.auth.UserInfo;
import ch.epfl.sdp.mocks.MockFragmentFactory;
import ch.epfl.sdp.ui.auth.AuthFragment;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
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
    public void MyFragment_Test_CanDisplayAMessage() {
        when(mAuthenticatorMock.getCurrentUser()).thenReturn(new UserInfo("Uid","Name", "Email"));
        FragmentScenario<AuthFragment> scenario = FragmentScenario.launchInContainer(
                AuthFragment.class,
                new Bundle(),
                R.style.Theme_AppCompat,
                new MockFragmentFactory(AuthFragment.class, mAuthenticatorMock, Uri.parse("https://eventum.com/D6ONoAKeGRAtAGJ9hBC1/"))
        );

        onView(withId(R.id.imageView)).check(matches((isDisplayed())));
    }

    @Test
    public void sharing(){
        Sharing sharing = new Sharing(Arrays.asList("anyRef"));
        sharing.getShareIntent();
    }

    @Test
    public void sharingBuilder(){
        SharingBuilder sharingBuilder = new SharingBuilder();
        sharingBuilder.setRef("anyRef");
        sharingBuilder.build();
    }
}
