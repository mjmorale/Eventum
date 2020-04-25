package ch.epfl.sdp.ui.settings;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import ch.epfl.sdp.future.Future;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.RootMatchers.isFocusable;
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

        verify(mDocumentQuery).update(eq("username"), eq(DUMMY_USER.getName()));
    }

    @Test
    public void AccountFragment_SetUsernameFailsWithToast() {

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

        verify(mDocumentQuery).delete();
    }

    @Test
    public void AccountFragment_DeleteAccountLogOutOnSuccess() {

    }

    @Test
    public void AccountFragment_DeleteAccountToastOnFailure() {

    }
}
