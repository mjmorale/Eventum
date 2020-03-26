package ch.epfl.sdp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.AuthCredential;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import ch.epfl.sdp.auth.AuthenticationResult;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.auth.User;

@RunWith(MockitoJUnitRunner.class)
public class LoginAuthViewModelTest {

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Mock
    private Authenticator<String> mAuthenticator;

    //@Mock
    //private String mString;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = IllegalArgumentException.class)
    public void LoginAuthViewModel_ConstructorFailsIfParameterIsNull() {
        LoginAuthViewModel<String> mLoginAuthViewModel = new LoginAuthViewModel<String>(null);
    }

    @Test
    public void LoginAuthViewModel_ConstructorSucceed() {
        LoginAuthViewModel<String> mLoginAuthViewModel = new LoginAuthViewModel<String>(mAuthenticator);
    }

    @Test
    public void LoginAuthViewModel_LoginSucceed() {
        LoginAuthViewModel<String> mLoginAuthViewModel = new LoginAuthViewModel<String>(mAuthenticator);
        String mString = "test";
        mLoginAuthViewModel.login(mString);
    }

    @Test
    public void LoginAuthViewModel_getUser() {
        LoginAuthViewModel<String> mLoginAuthViewModel = new LoginAuthViewModel<String>(mAuthenticator);
        LiveData<User> result = mLoginAuthViewModel.getUser();
    }
}
