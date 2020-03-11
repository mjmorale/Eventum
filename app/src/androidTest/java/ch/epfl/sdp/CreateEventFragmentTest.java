package ch.epfl.sdp;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sdp.ui.event.CreateEventFragment;

@RunWith(AndroidJUnit4.class)
public class CreateEventFragmentTest {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testEventFragment() {
        mActivityRule.getActivity().runOnUiThread(() -> {
            startCreateEventFragment();
        });
    }

    private CreateEventFragment startCreateEventFragment() {
        CreateEventFragment createEventFragment = new CreateEventFragment();
        mActivityRule.getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, createEventFragment)
                .commitNow();
        return createEventFragment;
    }
}
