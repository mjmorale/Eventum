package ch.epfl.sdp;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
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

//    @Test
//    public void checkThatMyLocationButtonIsDisplayed() {
//        onView(withContentDescription("My Location")).check(matches((isDisplayed())));
//        onView(withContentDescription("My Location")).perform(click());
//    }

//    @Test
//    public void checkPermissionsTest() throws InterruptedException {
//        sleep(10000);
//        try {
//            UiDevice device = UiDevice.getInstance(getInstrumentation());
//            UiObject allowPermissions = device.findObject(new UiSelector()
//                    .clickable(true)
//                    .checkable(false)
//                    .index(0));
//            if (allowPermissions.exists()) {
//                allowPermissions.click();
//            }
//        } catch (Exception e){
//        }
//        sleep(10000);

//        onView(withId(R.id.mapView)).check(matches((isDisplayed())));
//        onView(withText("Swipe")).perform(click());
//        onView(withText("Map")).perform(click());
//        onView(withId(R.id.mapView)).check(matches((isDisplayed())));
//        onView(withContentDescription("My Location")).check(matches((isDisplayed())));
//        onView(withContentDescription("My Location")).perform(click());
//    }



}

