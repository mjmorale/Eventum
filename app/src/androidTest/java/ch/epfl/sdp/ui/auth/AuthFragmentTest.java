package ch.epfl.sdp.ui.auth;

import android.os.Bundle;

import com.google.firebase.auth.AuthCredential;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.testing.FragmentScenario;
import ch.epfl.sdp.R;
import ch.epfl.sdp.User;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.auth.UserInfo;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.DocumentQuery;
import ch.epfl.sdp.future.Future;
import ch.epfl.sdp.mocks.MockFragmentFactory;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthFragmentTest {

    private static final UserInfo DUMMY_USERINFO = new UserInfo("testuid", "testname", "testmail");

    @Mock
    private Database mDatabase;

    @Mock
    private Authenticator<AuthCredential> mAuthenticator;

    @Mock
    private CollectionQuery mCollectionQuery;

    @Mock
    private DocumentQuery mDocumentQuery;

    @Mock
    private Future<Boolean> mBooleanFuture;

    @Mock
    private Future<Void> mVoidFuture;

    @Mock
    private AuthFragment.OnAuthFragmentResultListener mAuthFragmentResultListener;

    @Captor
    private ArgumentCaptor<Future.FutureThen<Boolean>> mFutureThenBooleanArgumentCaptor;

    @Captor
    private ArgumentCaptor<Future.FutureThen<Void>> mFutureThenVoidArgumentCaptor;

    @Captor
    private ArgumentCaptor<User> mUserCaptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void AuthFragment_ButtonIsEnabledIfNoUserConnected() {
        when(mAuthenticator.getCurrentUserInfo()).thenReturn(null);

        FragmentScenario.launchInContainer(
            AuthFragment.class,
            new Bundle(),
            R.style.Theme_AppCompat,
            new MockFragmentFactory<>(AuthFragment.class, mAuthenticator, mDatabase)
        );

        onView(withId(R.id.btn_google_sign_in)).check(matches(isEnabled()));
    }

    @Test
    public void AuthFragment_ButtonIsDisabledIfUserConnected() {
        when(mAuthenticator.getCurrentUserInfo()).thenReturn(DUMMY_USERINFO);
        when(mDatabase.query(anyString())).thenReturn(mCollectionQuery);
        when(mCollectionQuery.document(anyString())).thenReturn(mDocumentQuery);
        when(mDocumentQuery.exists()).thenReturn(mBooleanFuture);
        when(mBooleanFuture.then(any())).thenReturn(mBooleanFuture);
        when(mBooleanFuture.except(any())).thenReturn(mBooleanFuture);

        FragmentScenario.launchInContainer(
                AuthFragment.class,
                new Bundle(),
                R.style.Theme_AppCompat,
                new MockFragmentFactory<>(AuthFragment.class, mAuthenticator, mDatabase)
        );

        onView(withId(R.id.btn_google_sign_in)).check(matches(not(isEnabled())));
    }

    @Test
    public void AuthFragment_CallsAuthListenerIfUserIsConnectedAndInDatabase() {
        when(mAuthenticator.getCurrentUserInfo()).thenReturn(DUMMY_USERINFO);
        when(mDatabase.query(anyString())).thenReturn(mCollectionQuery);
        when(mCollectionQuery.document(anyString())).thenReturn(mDocumentQuery);
        when(mDocumentQuery.exists()).thenReturn(mBooleanFuture);
        when(mBooleanFuture.then(mFutureThenBooleanArgumentCaptor.capture())).thenReturn(mBooleanFuture);
        when(mBooleanFuture.except(any())).thenReturn(mBooleanFuture);

        FragmentScenario scenario = FragmentScenario.launchInContainer(
                AuthFragment.class,
                new Bundle(),
                R.style.Theme_AppCompat,
                new MockFragmentFactory<>(AuthFragment.class, mAuthenticator, mDatabase)
        );

        setupAuthResultListener();
        scenario.onFragment(fragment -> {
            ((AuthFragment)fragment).setAuthListener(mAuthFragmentResultListener);
        });

        mFutureThenBooleanArgumentCaptor.getValue().then(true);
    }

    @Test
    public void AuthFragment_CreatesTheUserIfNotPresentInDatabase() {
        when(mAuthenticator.getCurrentUserInfo()).thenReturn(DUMMY_USERINFO);
        when(mDatabase.query(anyString())).thenReturn(mCollectionQuery);
        when(mCollectionQuery.document(anyString())).thenReturn(mDocumentQuery);
        when(mDocumentQuery.set(mUserCaptor.capture())).thenReturn(mVoidFuture);
        when(mVoidFuture.then(mFutureThenVoidArgumentCaptor.capture())).thenReturn(mVoidFuture);
        when(mVoidFuture.except(any())).thenReturn(mVoidFuture);
        when(mDocumentQuery.exists()).thenReturn(mBooleanFuture);
        when(mBooleanFuture.then(mFutureThenBooleanArgumentCaptor.capture())).thenReturn(mBooleanFuture);
        when(mBooleanFuture.except(any())).thenReturn(mBooleanFuture);

        FragmentScenario scenario = FragmentScenario.launchInContainer(
                AuthFragment.class,
                new Bundle(),
                R.style.Theme_AppCompat,
                new MockFragmentFactory<>(AuthFragment.class, mAuthenticator, mDatabase)
        );

        setupAuthResultListener();
        scenario.onFragment(fragment -> {
            ((AuthFragment)fragment).setAuthListener(mAuthFragmentResultListener);
        });

        mFutureThenBooleanArgumentCaptor.getValue().then(false);
        mFutureThenVoidArgumentCaptor.getValue().then(null);

        User user = mUserCaptor.getValue();
        assertEquals(DUMMY_USERINFO.getDisplayName(), user.getName());
        assertEquals(DUMMY_USERINFO.getEmail(), user.getEmail());
    }

    private void setupAuthResultListener() {
        doAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            assertEquals(DUMMY_USERINFO.getUid(), args[0]);

            return null;
        }).when(mAuthFragmentResultListener).onLoggedIn(anyString());
    }
}
