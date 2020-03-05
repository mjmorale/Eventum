package ch.epfl.sdp;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AuthActivityTest {

    @Rule
    public final ActivityTestRule<AuthActivity> mActivityRule = new ActivityTestRule<>(AuthActivity.class);

    @Test
    public void testCanGreetUsers() {
    }
}
