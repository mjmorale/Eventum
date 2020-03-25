package ch.epfl.sdp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.AuthCredential;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.when;

import ch.epfl.sdp.auth.AuthenticationResult;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.auth.User;

@RunWith(MockitoJUnitRunner.class)
public class LoginAuthViewModelTest {

    @Mock
    private Authenticator<String> mAuthenticator;

    @Mock
    private User mUser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = IllegalArgumentException.class)
    public void LoginAuthViewModel_ConstructorFailsIfParameterIsNull() {
        LoginAuthViewModel<String> mLoginAuthViewModel = new LoginAuthViewModel<String>(null);
    }


    /*
    static class MockAuthenticator implements Authenticator<String> {


        @Override
        public void login(@NonNull String email, @NonNull String password, @Nullable OnLoginCallback callback) {

        }

        @Override
        public void login(@NonNull String credential, @Nullable OnLoginCallback callback) {

        }

        @Override
        public void logout() {

        }

        @Override
        public User getCurrentUser() {
            return null;
        }
    }

    @Test
    public void LoginAuthViewModel_ConstructorSucceed() {
        User user1 = new User("t", "b", "c");
        //when(super(mAuthenticator)).thenReturn(user1);
        //Mockito.doNothing().when(mParcel).writeString(DUMMY_UID);
        mMockAuthenticator = new MockAuthenticator();
        Authenticator<String> mAuthenticator2 = null;
        LoginAuthViewModel<String> mLoginAuthViewModel = new LoginAuthViewModel<String>(MockAuthenticator);
    }*/
}
