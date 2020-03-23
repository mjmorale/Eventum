package ch.epfl.sdp;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.intent.Intents;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

public class AuthActivityTest {
    private UiDevice mUiDevice;

    @Rule
    public ActivityTestRule<AuthActivity> mActivityTestRule = new ActivityTestRule<>(AuthActivity.class);

    @Before
    public void before() throws Exception {
        Intents.init();
    }

    @Test
    public void authActivityTest() throws UiObjectNotFoundException {

        onView(withId(R.id.btn_google_sign_in)).perform(click());

        //click button google window
        mUiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject mText = mUiDevice.findObject(new UiSelector().textContains("@gmail.com"));
        mText.click();


        //time to respond
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        intended(hasComponent(MainActivity.class.getName()));
    }

}
