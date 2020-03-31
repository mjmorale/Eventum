package ch.epfl.sdp.ui.auth;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import ch.epfl.sdp.User;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.ui.auth.AuthViewModel;

@RunWith(MockitoJUnitRunner.class)
public class AuthViewModelTest {

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
        AuthViewModel<String> mLoginAuthViewModel = new AuthViewModel<String>(null);
    }

    @Test
    public void LoginAuthViewModel_ConstructorSucceed() {
        AuthViewModel<String> mLoginAuthViewModel = new AuthViewModel<String>(mAuthenticator);
    }

    @Test
    public void LoginAuthViewModel_LoginSucceed() {
        AuthViewModel<String> mLoginAuthViewModel = new AuthViewModel<String>(mAuthenticator);
        String mString = "test";
        mLoginAuthViewModel.login(mString);
    }

    @Test
    public void LoginAuthViewModel_getUser() {
        AuthViewModel<String> mLoginAuthViewModel = new AuthViewModel<String>(mAuthenticator);
        LiveData<User> result = mLoginAuthViewModel.getUser();
    }
}
