package ch.epfl.sdp;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;

import com.google.firebase.auth.AuthCredential;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.runner.AndroidJUnit4;
import ch.epfl.sdp.auth.AuthenticationResult;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.auth.User;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasPackage;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
public class AuthActivityTest {

    private final static User DUMMY_USER = new User("uid", "name", "email");

    static class MockAuthenticator implements Authenticator<AuthCredential> {

        private User mUser;
        private OnLoginCallback mLoginCallback;

        MockAuthenticator(@Nullable User user, @Nullable OnLoginCallback loginCallback) {
            mUser = user;
            mLoginCallback = loginCallback;
        }

        @Override
        public void login(@NonNull String email, @NonNull String password, @Nullable OnLoginCallback callback) { }

        @Override
        public void login(@NonNull AuthCredential credential, @Nullable OnLoginCallback callback) {
            mLoginCallback.onLoginComplete(AuthenticationResult.success(mUser));
        }

        @Override
        public void logout() { }

        @Override
        public User getCurrentUser() {
            return mUser;
        }
    }

    static class MockLoginViewModel extends LoginAuthViewModel<AuthCredential> {
        public MockLoginViewModel(User user, Authenticator.OnLoginCallback callback) {
            super(new MockAuthenticator(user, callback));
        }
    }

    @Rule
    public final ActivityScenarioRule<AuthActivity> mScenarioRule = new ActivityScenarioRule<>(AuthActivity.class);

    @Test
    public void AuthActivity_test_LaunchesMainActivityWithCorrectUserData() {
        ActivityScenario<AuthActivity> scenario = mScenarioRule.getScenario();
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.onActivity(activity -> {
            activity.setViewModel(new MockLoginViewModel(DUMMY_USER, null));
        });
        Intents.init();
        scenario.moveToState(Lifecycle.State.RESUMED);

        intended(allOf(hasComponent("ch.epfl.sdp.MainActivity"), hasExtra(AuthActivity.USER_EXTRA, DUMMY_USER)));

        Intents.release();
    }

    @Test
    public void AuthActivity_test_GoogleSignInButtonIsEnabledIfNoConnectedUser() {
        ActivityScenario<AuthActivity> scenario = mScenarioRule.getScenario();
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.onActivity(activity -> {
            activity.setViewModel(new MockLoginViewModel(null, null));
        });
        scenario.moveToState(Lifecycle.State.RESUMED);

        onView(withId(R.id.btn_google_sign_in)).check(matches(isEnabled()));
    }

    @Test
    public void AuthActivity_test_GoogleSignInButtonLaunchesCorrectIntent() {
        ActivityScenario<AuthActivity> scenario = mScenarioRule.getScenario();
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.onActivity(activity -> {
            activity.setViewModel(new MockLoginViewModel(null, null));
        });
        Intents.init();
        scenario.moveToState(Lifecycle.State.RESUMED);

        Intent intent = new Intent();
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, intent);
        intending(allOf(hasAction("com.google.android.gms.auth.GOOGLE_SIGN_IN"), hasPackage("ch.epfl.sdp"))).respondWith(result);

        onView(withId(R.id.btn_google_sign_in)).perform(click());

        intended(allOf(hasAction("com.google.android.gms.auth.GOOGLE_SIGN_IN"), hasPackage("ch.epfl.sdp")));

        Intents.release();
    }
}
