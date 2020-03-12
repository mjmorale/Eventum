package ch.epfl.sdp.firebase.auth;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import ch.epfl.sdp.auth.User;
import ch.epfl.sdp.db.DatabaseObjectBuilderFactory;
import ch.epfl.sdp.firebase.auth.FirebaseAuthenticator;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FirebaseAuthenticatorTest {

    @Mock
    private FirebaseAuth mAuth;

    @Mock
    private FirebaseUser mUser;

    @Mock
    private Task<AuthResult> mAuthResultTask;

    @Mock
    private AuthCredential mAuthCredential;

    @Captor
    private ArgumentCaptor<OnCompleteListener<AuthResult>> authResultCompleteListenerCaptor;

    private final static String DUMMY_UID = "123456789abcdef";
    private final static String DUMMY_NAME = "name surname";
    private final static String DUMMY_EMAIL = "name.surname@mail.com";
    private final static String DUMMY_PASSWORD = "password";

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DatabaseObjectBuilderFactory.clear();
    }

    @Test(expected = IllegalArgumentException.class)
    public void firebaseAuthenticator_ConstructorFailsIfParameterIsNull() {
        FirebaseAuthenticator authenticator = new FirebaseAuthenticator(null);
    }

    @Test
    public void firebaseAuthenticator_GetCurrentUser_ReturnsNullIfNoUserConnected() {
        when(mAuth.getCurrentUser()).thenReturn(null);

        FirebaseAuthenticator authenticator = new FirebaseAuthenticator(mAuth);

        assertNull(authenticator.getCurrentUser());
    }

    @Test
    public void firebaseAuthenticator_GetCurrentUser_ReturnsCorrectUserIfConnected() {
        when(mUser.getUid()).thenReturn(DUMMY_UID);
        when(mUser.getDisplayName()).thenReturn(DUMMY_NAME);
        when(mUser.getEmail()).thenReturn(DUMMY_EMAIL);
        when(mAuth.getCurrentUser()).thenReturn(mUser);

        User expectedUser = new User(DUMMY_UID, DUMMY_NAME, DUMMY_EMAIL);

        FirebaseAuthenticator authenticator = new FirebaseAuthenticator(mAuth);

        assertEquals(expectedUser, authenticator.getCurrentUser());
    }

    @Test
    public void firebaseAuthenticator_Logout_DoesNotThrow() {
        doNothing().when(mAuth).signOut();

        FirebaseAuthenticator authenticator = new FirebaseAuthenticator(mAuth);

        authenticator.logout();
    }

    @Test(expected = IllegalArgumentException.class)
    public void firebaseAuthenticator_Login_FailsWithNullEmail() {
        FirebaseAuthenticator authenticator = new FirebaseAuthenticator(mAuth);

        authenticator.login(null, DUMMY_PASSWORD, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void firebaseAuthenticator_Login_FailsWithNullPassword() {
        FirebaseAuthenticator authenticator = new FirebaseAuthenticator(mAuth);

        authenticator.login(DUMMY_EMAIL, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void firebaseAuthenticator_Login_FailsWithNullCredentials() {
        FirebaseAuthenticator authenticator = new FirebaseAuthenticator(mAuth);

        authenticator.login(null, null);
    }

    @Test
    public void firebaseAuthenticator_Login_SuccessfullyLogInUserWithCorrectMail() {
        when(mAuth.createUserWithEmailAndPassword(DUMMY_EMAIL, DUMMY_PASSWORD)).thenReturn(mAuthResultTask);
        when(mAuth.getCurrentUser()).thenReturn(mUser);
        when(mUser.getUid()).thenReturn(DUMMY_UID);
        when(mUser.getDisplayName()).thenReturn(DUMMY_NAME);
        when(mUser.getEmail()).thenReturn(DUMMY_EMAIL);
        when(mAuthResultTask.isSuccessful()).thenReturn(true);
        when(mAuthResultTask.addOnCompleteListener(authResultCompleteListenerCaptor.capture())).thenReturn(null);

        FirebaseAuthenticator authenticator = new FirebaseAuthenticator(mAuth);

        User expectedUser = new User(DUMMY_UID, DUMMY_NAME, DUMMY_EMAIL);
        authenticator.login(DUMMY_EMAIL, DUMMY_PASSWORD, result -> {
            assertTrue(result.isSuccessful());
            assertEquals(expectedUser, result.getUser());
        });

        authResultCompleteListenerCaptor.getValue().onComplete(mAuthResultTask);
    }

    @Test
    public void firebaseAuthenticator_Login_FailureReturnNullUserAndCorrectException() {
        Exception exception = new Exception("dummy exception");
        when(mAuth.createUserWithEmailAndPassword(DUMMY_EMAIL, DUMMY_PASSWORD)).thenReturn(mAuthResultTask);
        when(mAuthResultTask.isSuccessful()).thenReturn(false);
        when(mAuthResultTask.getException()).thenReturn(exception);
        when(mAuthResultTask.addOnCompleteListener(authResultCompleteListenerCaptor.capture())).thenReturn(null);

        FirebaseAuthenticator authenticator = new FirebaseAuthenticator(mAuth);

        authenticator.login(DUMMY_EMAIL, DUMMY_PASSWORD, result -> {
            assertFalse(result.isSuccessful());
            assertEquals(exception, result.getException());
        });

        authResultCompleteListenerCaptor.getValue().onComplete(mAuthResultTask);
    }

    @Test
    public void firebaseAuthenticator_Login_DoNothingWhenCallbackIsNull() {
        when(mAuth.createUserWithEmailAndPassword(DUMMY_EMAIL, DUMMY_PASSWORD)).thenReturn(mAuthResultTask);
        verify(mAuthResultTask, never()).addOnCompleteListener(any(OnCompleteListener.class));

        FirebaseAuthenticator authenticator = new FirebaseAuthenticator(mAuth);

        authenticator.login(DUMMY_EMAIL, DUMMY_PASSWORD, null);
    }

    @Test
    public void firebaseAuthenticator_LoginWithCredential_SuccessfullyLogInUserWithCorrectCredential() {
        when(mAuth.signInWithCredential(any(AuthCredential.class))).thenReturn(mAuthResultTask);
        when(mAuth.getCurrentUser()).thenReturn(mUser);
        when(mUser.getUid()).thenReturn(DUMMY_UID);
        when(mUser.getDisplayName()).thenReturn(DUMMY_NAME);
        when(mUser.getEmail()).thenReturn(DUMMY_EMAIL);
        when(mAuthResultTask.isSuccessful()).thenReturn(true);
        when(mAuthResultTask.addOnCompleteListener(authResultCompleteListenerCaptor.capture())).thenReturn(null);

        FirebaseAuthenticator authenticator = new FirebaseAuthenticator(mAuth);

        User expectedUser = new User(DUMMY_UID, DUMMY_NAME, DUMMY_EMAIL);
        authenticator.login(mAuthCredential, result -> {
            assertTrue(result.isSuccessful());
            assertEquals(expectedUser, result.getUser());
        });

        authResultCompleteListenerCaptor.getValue().onComplete(mAuthResultTask);
    }

    @Test
    public void firebaseAuthenticator_LoginWithCredential_FailureReturnNullUserAndCorrectException() {
        Exception exception = new Exception("dummy exception");
        when(mAuth.signInWithCredential(any(AuthCredential.class))).thenReturn(mAuthResultTask);
        when(mAuthResultTask.isSuccessful()).thenReturn(false);
        when(mAuthResultTask.getException()).thenReturn(exception);
        when(mAuthResultTask.addOnCompleteListener(authResultCompleteListenerCaptor.capture())).thenReturn(null);

        FirebaseAuthenticator authenticator = new FirebaseAuthenticator(mAuth);

        authenticator.login(mAuthCredential, result -> {
            assertFalse(result.isSuccessful());
            assertEquals(exception, result.getException());
        });

        authResultCompleteListenerCaptor.getValue().onComplete(mAuthResultTask);
    }

    @Test
    public void firebaseAuthenticator_LoginWithCredential_DoNothingWhenCallbackIsNull() {
        when(mAuth.signInWithCredential(any(AuthCredential.class))).thenReturn(mAuthResultTask);
        verify(mAuthResultTask, never()).addOnCompleteListener(any(OnCompleteListener.class));

        FirebaseAuthenticator authenticator = new FirebaseAuthenticator(mAuth);

        authenticator.login(mAuthCredential, null);
    }
}
