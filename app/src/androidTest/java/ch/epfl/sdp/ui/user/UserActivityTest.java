package ch.epfl.sdp.ui.user;

import android.content.Intent;

import com.google.firebase.auth.AuthCredential;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import ch.epfl.sdp.Event;
import ch.epfl.sdp.User;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.auth.UserInfo;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.DatabaseObject;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.DocumentQuery;
import ch.epfl.sdp.db.queries.FilterQuery;
import ch.epfl.sdp.ui.ServiceProvider;
import ch.epfl.sdp.utils.TestUtils;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserActivityTest {

    private final static UserInfo DUMMY_USERINFO = new UserInfo("sdkfgjhlkj", "testname", "testemail");

    @Mock
    private Database mDatabase;

    @Mock
    private CollectionQuery mCollectionQuery;

    @Mock
    private DocumentQuery mDocumentQuery;
    @Mock
    private FilterQuery mFilterQuery;
    @Mock
    private LiveData<User> mUserLiveData;

    @Mock
    private Authenticator<AuthCredential> mAuthenticator;

    private LiveData<List<DatabaseObject<Event>>> mEventsLiveData = new MutableLiveData<>();

    @Rule
    public ActivityTestRule<UserActivity> mActivity = new ActivityTestRule<>(UserActivity.class, false, false);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(mAuthenticator.getCurrentUser()).thenReturn(DUMMY_USERINFO);
        when(mDatabase.query(anyString())).thenReturn(mCollectionQuery);
        when(mCollectionQuery.whereFieldEqualTo(anyString(), any())).thenReturn(mFilterQuery);
        when(mFilterQuery.liveData(Event.class)).thenReturn(mEventsLiveData);
        when(mCollectionQuery.document(anyString())).thenReturn(mDocumentQuery);
        when(mDocumentQuery.liveData(User.class)).thenReturn(mUserLiveData);
        ServiceProvider.getInstance().setDatabase(mDatabase);
        ServiceProvider.getInstance().setAuthenticator(mAuthenticator);

        mActivity.launchActivity(new Intent());
    }

    @Test
    public void UserActivity_DisplayUserStatisticsAsDefault() {
        onView(withText("My Profile")).check(matches(isDisplayed()));
    }

    @Test
    public void UserActivity_SecondTabDisplaysMyEvents() {
        onView(withText("My events")).perform(click());
        onView(withText("You haven't created any events.")).check(matches(isDisplayed()));
    }
}
