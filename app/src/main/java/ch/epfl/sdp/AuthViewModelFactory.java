package ch.epfl.sdp;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import ch.epfl.sdp.auth.Authenticator;

public class AuthViewModelFactory<CredType> implements ViewModelProvider.Factory  {

    private final Authenticator<CredType> mAuthenticator;

    public AuthViewModelFactory(@NonNull Authenticator<CredType> authenticator) {
        if(authenticator == null) {
            throw new IllegalArgumentException();
        }
        mAuthenticator = authenticator;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AuthViewModel.class)) {
            return (T) new AuthViewModel(mAuthenticator);
        }
        throw new IllegalArgumentException("ViewModel class is not a subclass of AuthViewModel");
    }
}
