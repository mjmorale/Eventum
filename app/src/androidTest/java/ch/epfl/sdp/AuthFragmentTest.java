package ch.epfl.sdp;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sdp.ui.main.AuthFragment;

@RunWith(AndroidJUnit4.class)
public class AuthFragmentTest {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    private AuthFragment setupFragment() {
        AuthFragment authFragment = new AuthFragment();
        mActivityRule.getActivity().getSupportFragmentManager()
                .beginTransaction()
                .add(authFragment, "AuthFragment")
                .commitNow();
        return authFragment;
    }

    @Test
    public void authFragment_Create() {
        mActivityRule.getActivity().runOnUiThread(() -> {
            AuthFragment authFragment = setupFragment();
        });
    }
}
