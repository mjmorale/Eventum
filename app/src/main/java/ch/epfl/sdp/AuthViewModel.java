package ch.epfl.sdp;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ch.epfl.sdp.auth.AuthenticationResult;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.auth.User;

public class AuthViewModel<CredType> extends ViewModel {

    private final static String TAG = "AuthViewModel";

    private final Authenticator<CredType> mAuthenticator;
    private MutableLiveData<User> mUser = new MutableLiveData<>();

    public AuthViewModel(@NonNull Authenticator<CredType> authenticator) {
        if(authenticator == null) {
            throw new IllegalArgumentException();
        }
        mAuthenticator = authenticator;
        mUser.postValue(mAuthenticator.getCurrentUser());
    }

    public void login(CredType credential) {
        mAuthenticator.login(credential, result -> {
            if(result.isSuccessful()) {
                mUser.postValue(result.getUser());
            }
            else {
                Log.e(TAG, "Cannot log in", result.getException());
            }
        });
    }

    public LiveData<User> getUser() {
        return mUser;
    }
}
