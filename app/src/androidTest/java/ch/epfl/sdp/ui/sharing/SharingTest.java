package ch.epfl.sdp.ui.sharing;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import ch.epfl.sdp.User;
import ch.epfl.sdp.auth.Authenticator;
import java.util.ArrayList;
import java.util.Arrays;

import ch.epfl.sdp.R;
import ch.epfl.sdp.mocks.MockFragmentFactory;
import ch.epfl.sdp.ui.auth.AuthActivity;
import ch.epfl.sdp.ui.auth.AuthFragment;
import ch.epfl.sdp.ui.main.MainActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
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
    public void MyFragment_Test_CanDisplayAMessage() {
        when(mAuthenticatorMock.getCurrentUser()).thenReturn(new User("Uid","Name", "Email"));
        Bundle bundle = new Bundle();
        bundle.putString("uri","https://eventum.com/D6ONoAKeGRAtAGJ9hBC1/");
        FragmentScenario<AuthFragment> scenario = FragmentScenario.launchInContainer(
                AuthFragment.class,
                bundle,
                R.style.Theme_AppCompat,
                new MockFragmentFactory(AuthFragment.class, mAuthenticatorMock,Uri.parse("https://eventum.com/D6ONoAKeGRAtAGJ9hBC1/"))
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
