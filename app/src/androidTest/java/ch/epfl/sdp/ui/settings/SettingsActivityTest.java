package ch.epfl.sdp.ui.settings;

import android.content.Intent;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import androidx.lifecycle.MutableLiveData;
import androidx.test.espresso.intent.Intents;
import androidx.test.rule.ActivityTestRule;
import ch.epfl.sdp.User;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.DocumentQuery;
import ch.epfl.sdp.ui.ServiceProvider;
import ch.epfl.sdp.ui.UIConstants;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SettingsActivityTest {

    private final static String DUMMY_USERREF = "dsflkgjhq3p4o5iuh";

    @Mock
    private Database mDatabase;

    @Mock
    private CollectionQuery mCollectionQuery;

    @Mock
    private DocumentQuery mDocumentQuery;

    private MutableLiveData<User> mUserLiveData = new MutableLiveData<>();

    @Rule
    public ActivityTestRule<SettingsActivity> mActivity = new ActivityTestRule<>(SettingsActivity.class, false, false);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        Intent intent = new Intent();
        intent.putExtra(UIConstants.BUNDLE_USER_REF, DUMMY_USERREF);

        when(mDatabase.query(anyString())).thenReturn(mCollectionQuery);
        when(mCollectionQuery.document(anyString())).thenReturn(mDocumentQuery);
        when(mDocumentQuery.liveData(User.class)).thenReturn(mUserLiveData);
        ServiceProvider.getInstance().setDatabase(mDatabase);

        mActivity.launchActivity(intent);
    }

    @Test
    public void SettingsActivity_HasAccountMenu() {
        onView(withText("Account")).check(matches(isDisplayed()));
    }

    @Test
    public void SettingsActivity_HasLogOutMenu() {
        onView(withText("Log out")).check(matches(isDisplayed()));
    }

    @Test
    public void SettingsActivity_AccountMenuHasAvatar() {
        onView(withText("Account")).perform(click());
        onView(withText("Avatar")).check(matches(isDisplayed()));
    }

    @Test
    public void SettingsActivity_AccountMenuHasName() {
        onView(withText("Account")).perform(click());
        onView(withText("Name")).check(matches(isDisplayed()));
    }

    @Test
    public void SettingsActivity_AccountMenuHasEMail() {
        onView(withText("Account")).perform(click());
        onView(withText("E-mail")).check(matches(isDisplayed()));
    }

    @Test
    public void SettingsActivity_AccountMenuHasDelete() {
        onView(withText("Account")).perform(click());
        onView(withText("Delete account")).check(matches(isDisplayed()));
    }

    @Test
    public void SettingsActivity_OnLogoutLaunchesAuthActivity() {
        Intents.init();

        mActivity.getActivity().onLogout();

        intended(hasComponent("ch.epfl.sdp.ui.auth.AuthActivity"));

        Intents.release();
    }
}
