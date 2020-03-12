package ch.epfl.sdp;

import android.Manifest;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import com.google.android.gms.maps.model.VisibleRegion;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MapTest{
    @Rule public GrantPermissionRule permissionRule1 = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);
    @Rule public GrantPermissionRule permissionRule2 = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_COARSE_LOCATION);

    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void setup(){
        onView(withText("Map")).perform(click());
    }

    @Test
    public void checkThatMapIsDisplayed() {
        onView(withId(R.id.mapView)).check(matches((isDisplayed())));
    }

    @Test
    public void checkThatMyLocationButtonIsDisplayed() {
        onView(withContentDescription("My Location")).check(matches((isDisplayed())));
        onView(withContentDescription("My Location")).perform(click());
    }

    @Before
    void revokePermissions() {
        InstrumentationRegistry.getInstrumentation().getUiAutomation().revokeRuntimePermission(
                InstrumentationRegistry.getInstrumentation().getTargetContext().getPackageName(),
                Manifest.permission.ACCESS_COARSE_LOCATION);

        InstrumentationRegistry.getInstrumentation().getUiAutomation().revokeRuntimePermission(
                InstrumentationRegistry.getInstrumentation().getTargetContext().getPackageName(),
                Manifest.permission.ACCESS_FINE_LOCATION);
    }
}

