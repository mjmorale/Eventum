package ch.epfl.sdp;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import ch.epfl.sdp.auth.Authenticator;

public abstract class AuthViewModel<CredType> extends ViewModel {

    protected final Authenticator<CredType> mAuthenticator;

    public AuthViewModel(@NonNull Authenticator<CredType> authenticator) {
        if(authenticator == null) {
            throw new IllegalArgumentException();
        }
        mAuthenticator = authenticator;
    }
}
