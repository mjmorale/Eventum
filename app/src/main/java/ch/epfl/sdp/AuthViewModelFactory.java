package ch.epfl.sdp;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

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
        if (AuthViewModel.class.isAssignableFrom(modelClass)) {
            try {
                Constructor<T> constructor = modelClass.getConstructor(Authenticator.class);
                return constructor.newInstance(mAuthenticator);
            }
            catch(NoSuchMethodException e) {
                throw new IllegalArgumentException(modelClass.getSimpleName() + " does not have a constructor that only takes an Authenticator");
            }
            catch(IllegalAccessException | InstantiationException | InvocationTargetException e) {
                throw new IllegalArgumentException("Cannot instantiate an instance of the class " + modelClass.getSimpleName());
            }
        }
        throw new IllegalArgumentException("ViewModel class is not a superclass of AuthViewModel");
    }
}
