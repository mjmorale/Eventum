package ch.epfl.sdp;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.firebase.auth.FirebaseAuthenticator;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FirebaseAuthViewModelFactoryTest {
    @Mock
    private FirebaseAuth mAuth;

    @Mock
    private Authenticator<String> mAuthenticator;

    private FirebaseAuthenticator mFirebaseAuthenticator;
    private FirebaseAuth mockedDatabaseReference;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void FirebaseAuthViewModelFactory_ConstructorFailsIfParameterIsNull() {
        FirebaseAuth mockedFirebase = mock(FirebaseAuth.class);
        //when(new FirebaseAuthViewModelFactory()).thenReturn(null);
        //doNothing().when(mAuth).signOut();
        //when(new FirebaseAuthenticator(mAuth)).thenReturn(new Authenticator<String>());

        //FirebaseAuthViewModelFactory viewModelFactory = FirebaseAuthViewModelFactory.getInstance();
    }
}
