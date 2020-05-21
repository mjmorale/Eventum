package ch.epfl.sdp.ui.settings;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import androidx.test.espresso.intent.Intents;
import ch.epfl.sdp.db.queries.QueryResult;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.RootMatchers.isFocusable;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AccountFragmentTest extends SettingsFragmentTest {

    @Before
    @Override
    public void setup() {
        super.setup();

        onView(withText("Account")).perform(click());
    }

    @Test
    public void AccountFragment_DisplayCorrectUserInfo() throws Throwable {
        mActivity.runOnUiThread(() -> {
            mUserLiveData.setValue(DUMMY_USER);
        });

        onView(withText(DUMMY_USER.getName())).check(matches(isDisplayed()));
        onView(withText(DUMMY_USER.getEmail())).check(matches(isDisplayed()));
    }


    @Test
    public void AccountFragment_DeleteAccountCreatesDialog() {

        onView(withText("Delete account")).perform(click());

        onView(withText("Delete Account"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
    }

    @Test
    public void AccountFragment_DeleteAccountCallsDatabase() {

        onView(withText("Delete account")).perform(click());

        onView(withText("YES"))
                .inRoot(isDialog())
                .perform(click());

        verify(mDocumentQuery).delete(any());
    }

    @Test
    public void AccountFragment_DeleteAccountLogOutOnSuccess() throws Throwable {
        doNothing().when(mDocumentQuery).delete(mOnQueryCompleteCallbackArgumentCaptor.capture());

        onView(withText("Delete account")).perform(click());

        onView(withText("YES"))
                .inRoot(isDialog())
                .perform(click());

        Intents.init();

        intending(hasComponent("ch.epfl.sdp.ui.auth.AuthActivity"))
                .respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, new Intent()));

        mActivity.runOnUiThread(() -> {
            mOnQueryCompleteCallbackArgumentCaptor.getValue().onQueryComplete(QueryResult.success(null));
        });

        verify(mAuthenticator).logout();

        Intents.release();
    }

    @Test
    public void AccountFragment_DeleteAccountToastOnFailure() throws Throwable {

        doNothing().when(mDocumentQuery).delete(mOnQueryCompleteCallbackArgumentCaptor.capture());

        onView(withText("Delete account")).perform(click());

        onView(withText("YES"))
                .inRoot(isDialog())
                .perform(click());

        mActivity.runOnUiThread(() -> {
            mOnQueryCompleteCallbackArgumentCaptor.getValue().onQueryComplete(QueryResult.failure(new Exception()));
        });

        onView(withText("Cannot delete account"))
                .inRoot(not(isFocusable()))
                .check(matches(isDisplayed()));
    }
}
