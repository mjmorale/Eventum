package ch.epfl.sdp.ui.auth;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ch.epfl.sdp.User;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.ui.ParameterizedViewModelFactory;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class AuthViewModel<CredType> extends ViewModel {

    static class AuthViewModelFactory extends ParameterizedViewModelFactory {

        AuthViewModelFactory() {
            super(Authenticator.class);
        }

        void setAuthenticator(@NonNull Authenticator authenticator) {
            setValue(0, verifyNotNull(authenticator));
        }
    }

    private final static String TAG = "LoginAuthViewModel";

    private final Authenticator<CredType> mAuthenticator;
    private final MutableLiveData<User> mUser = new MutableLiveData<>();

    public AuthViewModel(@NonNull Authenticator<CredType> authenticator) {
        mAuthenticator = verifyNotNull(authenticator);
        mUser.postValue(mAuthenticator.getCurrentUser());
    }

    public void login(CredType credential) {
        mAuthenticator.login(credential, result -> {
            if(result.isSuccessful()) { mUser.postValue(result.getUser());}
            else { Log.e(TAG, "Cannot log in", result.getException());}
        });
    }

    @NonNull
    public LiveData<User> getUser() {
        return mUser;
    }
}
