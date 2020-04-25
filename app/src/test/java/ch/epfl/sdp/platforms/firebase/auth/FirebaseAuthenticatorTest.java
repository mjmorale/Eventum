package ch.epfl.sdp.platforms.firebase.auth;

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

import ch.epfl.sdp.auth.UserInfo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
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
    }

    @Test(expected = IllegalArgumentException.class)
    public void firebaseAuthenticator_ConstructorFailsIfParameterIsNull() {
        FirebaseAuthenticator authenticator = new FirebaseAuthenticator(null);
    }

    @Test
    public void firebaseAuthenticator_GetCurrentUserInfo_ReturnsNullIfNoUserConnected() {
        when(mAuth.getCurrentUser()).thenReturn(null);

        FirebaseAuthenticator authenticator = new FirebaseAuthenticator(mAuth);

        assertNull(authenticator.getCurrentUserInfo());
    }

    @Test
    public void firebaseAuthenticator_GetCurrentUserInfo_ReturnsCorrectUserIfConnected() {
        when(mUser.getUid()).thenReturn(DUMMY_UID);
        when(mUser.getDisplayName()).thenReturn(DUMMY_NAME);
        when(mUser.getEmail()).thenReturn(DUMMY_EMAIL);
        when(mAuth.getCurrentUser()).thenReturn(mUser);

        UserInfo expectedUserInfo = new UserInfo(DUMMY_UID, DUMMY_NAME, DUMMY_EMAIL);

        FirebaseAuthenticator authenticator = new FirebaseAuthenticator(mAuth);

        assertEquals(expectedUserInfo, authenticator.getCurrentUserInfo());
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

        authenticator.login(null, DUMMY_PASSWORD);
    }

    @Test(expected = IllegalArgumentException.class)
    public void firebaseAuthenticator_Login_FailsWithNullPassword() {
        FirebaseAuthenticator authenticator = new FirebaseAuthenticator(mAuth);

        authenticator.login(DUMMY_EMAIL, null);
    }
}
