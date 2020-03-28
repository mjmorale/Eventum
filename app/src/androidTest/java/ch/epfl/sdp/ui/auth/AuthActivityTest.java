package ch.epfl.sdp.ui.auth;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;

import com.google.firebase.auth.AuthCredential;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.runner.AndroidJUnit4;
import ch.epfl.sdp.User;
import ch.epfl.sdp.auth.AuthenticationResult;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.ui.auth.AuthActivity;
import ch.epfl.sdp.ui.auth.AuthViewModel;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasPackage;
import static org.hamcrest.Matchers.allOf;

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

    static class MockLoginViewModel extends AuthViewModel<AuthCredential> {
        public MockLoginViewModel(User user, Authenticator.OnLoginCallback callback) {
            super(new MockAuthenticator(user, callback));
        }
    }

    @Test
    public void AuthActivity_test_launch() {
        Intents.init();

        Intent intent = new Intent();
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, intent);
        intending(hasComponent("ch.epfl.sdp.MainActivity")).respondWith(result);
        intending(allOf(hasPackage("ch.epfl.sdp"), hasAction("com.google.android.gms.auth.GOOGLE_SIGN_IN"))).respondWith(result);

        ActivityScenario<AuthActivity> scenario = ActivityScenario.launch(AuthActivity.class);

        Intents.release();
    }
}
