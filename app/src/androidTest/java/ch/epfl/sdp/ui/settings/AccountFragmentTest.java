package ch.epfl.sdp.ui.settings;

import android.content.Intent;

import com.google.firebase.auth.AuthCredential;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import androidx.lifecycle.MutableLiveData;
import androidx.test.rule.ActivityTestRule;
import ch.epfl.sdp.User;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.DocumentQuery;
import ch.epfl.sdp.db.queries.Query;
import ch.epfl.sdp.db.queries.QueryResult;
import ch.epfl.sdp.ui.ServiceProvider;
import ch.epfl.sdp.ui.UIConstants;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
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
import static org.mockito.Mockito.when;

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
    public void AccountFragment_SetUsernameChangesDatabase() throws Throwable {
        mActivity.runOnUiThread(() -> {
            mUserLiveData.setValue(DUMMY_USER);
        });

        onView(withText("Name")).perform(click());

        onView(withText("nametest")).perform(
            replaceText(DUMMY_USER.getName()),
            closeSoftKeyboard()
        );

        onView(withText("OK")).perform(click());

        verify(mDocumentQuery).update(eq("username"), eq(DUMMY_USER.getName()), any());
    }

    @Test
    public void AccountFragment_SetUsernameFailsWithToast() throws Throwable {
        mActivity.runOnUiThread(() -> {
            mUserLiveData.setValue(DUMMY_USER);
        });

        doNothing().when(mDocumentQuery).update(anyString(), any(), mOnQueryCompleteCallbackArgumentCaptor.capture());

        onView(withText("Name")).perform(click());

        onView(withText("nametest")).perform(
                replaceText(DUMMY_USER.getName()),
                closeSoftKeyboard()
        );

        onView(withText("OK")).perform(click());

        mActivity.runOnUiThread(() -> {
            mOnQueryCompleteCallbackArgumentCaptor.getValue().onQueryComplete(QueryResult.failure(new Exception()));
        });

        onView(withText("Cannot set username"))
                .inRoot(withDecorView(not(is(mActivity.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
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

        mActivity.runOnUiThread(() -> {
            mOnQueryCompleteCallbackArgumentCaptor.getValue().onQueryComplete(QueryResult.success(null));
        });

        verify(mAuthenticator).logout();
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
                .inRoot(withDecorView(not(is(mActivity.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }
}