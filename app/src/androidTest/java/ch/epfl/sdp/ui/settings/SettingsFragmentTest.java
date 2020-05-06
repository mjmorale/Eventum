package ch.epfl.sdp.ui.settings;

import android.content.Intent;

import androidx.lifecycle.MutableLiveData;
import androidx.test.rule.ActivityTestRule;

import com.google.firebase.auth.AuthCredential;

import org.junit.Rule;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ch.epfl.sdp.User;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.DocumentQuery;
import ch.epfl.sdp.db.queries.Query;
import ch.epfl.sdp.ui.ServiceProvider;
import ch.epfl.sdp.ui.UIConstants;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class SettingsFragmentTest {

    protected final static String DUMMY_USERREF = "kljsdfghqoli34u5";
    protected final static User DUMMY_USER = new User("nametest", "emailtest");

    @Mock
    protected Database mDatabase;

    @Mock
    protected CollectionQuery mCollectionQuery;

    @Mock
    protected DocumentQuery mDocumentQuery;

    @Mock
    protected Authenticator<AuthCredential> mAuthenticator;

    @Captor
    protected ArgumentCaptor<Query.OnQueryCompleteCallback<Void>> mOnQueryCompleteCallbackArgumentCaptor;

    protected MutableLiveData<User> mUserLiveData = new MutableLiveData<>();

    @Rule
    public ActivityTestRule<SettingsActivity> mActivity = new ActivityTestRule<>(SettingsActivity.class, false, false);

    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(mDatabase.query(anyString())).thenReturn(mCollectionQuery);
        when(mCollectionQuery.document(anyString())).thenReturn(mDocumentQuery);
        when(mDocumentQuery.liveData(User.class)).thenReturn(mUserLiveData);
        ServiceProvider.getInstance().setAuthenticator(mAuthenticator);
        ServiceProvider.getInstance().setDatabase(mDatabase);

        Intent intent = new Intent();
        intent.putExtra(UIConstants.BUNDLE_USER_REF, DUMMY_USERREF);
        mActivity.launchActivity(intent);
    }
}
