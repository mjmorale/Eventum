package ch.epfl.sdp.ui.auth;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.AuthCredential;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.test.espresso.intent.Intents;
import ch.epfl.sdp.R;
import ch.epfl.sdp.User;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.auth.UserInfo;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.DocumentQuery;
import ch.epfl.sdp.db.queries.Query;
import ch.epfl.sdp.db.queries.QueryResult;
import ch.epfl.sdp.mocks.MockFragmentFactory;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasPackage;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
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
    private AuthFragment.OnAuthFragmentResultListener mAuthFragmentResultListener;

    @Captor
    private ArgumentCaptor<Query.OnQueryCompleteCallback<Boolean>> mBooleanQueryCompleteCallbackCaptor;

    @Captor
    private ArgumentCaptor<Query.OnQueryCompleteCallback<Void>> mVoidQueryCompleteCallbackCaptor;

    @Captor
    private ArgumentCaptor<User> mUserCaptor;

    private MutableLiveData<UserInfo> mUserInfoLiveData = new MutableLiveData<>();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void AuthFragment_ButtonIsActiveIfNoUserConnected() {
        when(mAuthenticator.getCurrentUser()).thenReturn(null);

        FragmentScenario.launchInContainer(
            AuthFragment.class,
            new Bundle(),
            R.style.Theme_AppCompat,
            new MockFragmentFactory<>(AuthFragment.class, mAuthenticator, mDatabase)
        );

        onView(withId(R.id.btn_google_sign_in)).check(matches(isEnabled()));
    }

    @Test
    public void AuthFragment_ButtonIsInactiveIfUserConnected() {
        when(mAuthenticator.getCurrentUser()).thenReturn(DUMMY_USERINFO);
        when(mDatabase.query(anyString())).thenReturn(mCollectionQuery);
        when(mCollectionQuery.document(anyString())).thenReturn(mDocumentQuery);
        doNothing().when(mDocumentQuery).exists(any());

        FragmentScenario.launchInContainer(
            AuthFragment.class,
            new Bundle(),
            R.style.Theme_AppCompat,
            new MockFragmentFactory<>(AuthFragment.class, mAuthenticator, mDatabase)
        );

        onView(withId(R.id.btn_google_sign_in)).check(matches(not(isEnabled())));
    }

    @Test
    public void AuthFragment_LoginButtonStartsGoogleIntent() {
        Intents.init();

        when(mAuthenticator.getCurrentUser()).thenReturn(null);

        FragmentScenario.launchInContainer(
                AuthFragment.class,
                new Bundle(),
                R.style.Theme_AppCompat,
                new MockFragmentFactory<>(AuthFragment.class, mAuthenticator, mDatabase)
        );

        intending(allOf(hasAction("com.google.android.gms.auth.GOOGLE_SIGN_IN"), hasPackage("ch.epfl.sdp"))).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, new Intent()));

        onView(withId(R.id.btn_google_sign_in)).perform(click());

        intended(allOf(hasAction("com.google.android.gms.auth.GOOGLE_SIGN_IN"), hasPackage("ch.epfl.sdp")));

        Intents.release();
    }

    @Test
    public void AuthFragment_CallsAuthListenerIfUserIsConnected() {
        when(mAuthenticator.getCurrentUser()).thenReturn(DUMMY_USERINFO);
        when(mDatabase.query(anyString())).thenReturn(mCollectionQuery);
        when(mCollectionQuery.document(anyString())).thenReturn(mDocumentQuery);
        doNothing().when(mDocumentQuery).exists(mBooleanQueryCompleteCallbackCaptor.capture());
        setupAuthResultListener();

        FragmentScenario<AuthFragment> scenario = FragmentScenario.launchInContainer(
                AuthFragment.class,
                new Bundle(),
                R.style.Theme_AppCompat,
                new MockFragmentFactory<>(AuthFragment.class, mAuthenticator, mDatabase)
        );

        scenario.onFragment(fragment -> {
           fragment.setAuthListener(mAuthFragmentResultListener);
        });

        mBooleanQueryCompleteCallbackCaptor.getValue().onQueryComplete(QueryResult.success(true));
    }

    @Test
    public void AuthFragment_CreatesTheUserIfNotPresentInDatabase() {
        when(mAuthenticator.getCurrentUser()).thenReturn(DUMMY_USERINFO);
        when(mDatabase.query(anyString())).thenReturn(mCollectionQuery);
        when(mCollectionQuery.document(anyString())).thenReturn(mDocumentQuery);
        doNothing().when(mDocumentQuery).set(mUserCaptor.capture(), mVoidQueryCompleteCallbackCaptor.capture());
        doNothing().when(mDocumentQuery).exists(mBooleanQueryCompleteCallbackCaptor.capture());
        setupAuthResultListener();

        FragmentScenario<AuthFragment> scenario = FragmentScenario.launchInContainer(
                AuthFragment.class,
                new Bundle(),
                R.style.Theme_AppCompat,
                new MockFragmentFactory<>(AuthFragment.class, mAuthenticator, mDatabase)
        );

        scenario.onFragment(fragment -> {
            fragment.setAuthListener(mAuthFragmentResultListener);
        });

        mBooleanQueryCompleteCallbackCaptor.getValue().onQueryComplete(QueryResult.success(false));

        User user = mUserCaptor.getValue();
        assertEquals(DUMMY_USERINFO.getDisplayName(), user.getName());
        assertEquals(DUMMY_USERINFO.getEmail(), user.getEmail());

        mVoidQueryCompleteCallbackCaptor.getValue().onQueryComplete(QueryResult.success(null));
    }

    private void setupAuthResultListener() {
        doAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            assertEquals(DUMMY_USERINFO.getUid(), args[0]);

            return null;
        }).when(mAuthFragmentResultListener).onLoggedIn(anyString());
    }
}
