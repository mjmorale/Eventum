package ch.epfl.sdp;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import ch.epfl.sdp.firebase.auth.FirebaseAuthenticator;

public class FirebaseAuthViewModelFactory extends AuthViewModelFactory<AuthCredential> {

    private static FirebaseAuthViewModelFactory mInstance = null;

    private FirebaseAuthViewModelFactory() {
        super(new FirebaseAuthenticator(FirebaseAuth.getInstance()));
    }

    @NonNull
    public static FirebaseAuthViewModelFactory getInstance() {
        if(mInstance == null) { mInstance = new FirebaseAuthViewModelFactory();}
        return mInstance;
    }
}
