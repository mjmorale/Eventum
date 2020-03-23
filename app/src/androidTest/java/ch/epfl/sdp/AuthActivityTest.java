package ch.epfl.sdp;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.EditText;

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
import static org.junit.Assert.assertEquals;

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
    public void authActivityTest() throws UiObjectNotFoundException, InterruptedException {

        onView(withId(R.id.btn_google_sign_in)).perform(click());

        //click button google window
        Thread.sleep(7000);
        mUiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject emailInput = mUiDevice.findObject(new UiSelector().instance(0).className(EditText.class));

        emailInput.waitForExists(5000);
        emailInput.setText("eventum.app.test@gmail.com");

        UiObject mText = mUiDevice.findObject(new UiSelector().textContains("Next"));
        mText.waitForExists(5000);
        mText.click();

        // Set Password
        //mUiDevice.pressBack();
        UiObject passwordInput = mUiDevice.findObject(new UiSelector().instance(0).className(EditText.class));

        passwordInput.waitForExists(5000);
        passwordInput.setText("passwordfake");// type your password here

        // Confirm Button Click
        UiObject Next = mUiDevice.findObject(new UiSelector().textContains("Next"));
        Next.waitForExists(5000);
        Next.click();

        UiObject nextButton = mUiDevice.findObject(new UiSelector().textMatches("I agree"));

        nextButton.waitForExists(5000);
        nextButton.click();

        UiObject layout = mUiDevice.findObject(new UiSelector().resourceId("suw_layout_content"));
        layout.waitForExists(5000);

        UiObject buttonNext = mUiDevice.findObject(new UiSelector().className(Button.class));
        buttonNext.waitForExists(5000);
        buttonNext.click();
        Thread.sleep(7000);

        intended(hasComponent(MainActivity.class.getName()));
    }

}
