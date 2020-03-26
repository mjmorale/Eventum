package ch.epfl.sdp.ui.auth;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import ch.epfl.sdp.User;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.ui.AuthViewModel;

public class LoginAuthViewModel<CredType> extends AuthViewModel<CredType> {

    private final static String TAG = "LoginAuthViewModel";

    private MutableLiveData<User> mUser = new MutableLiveData<>();

    public LoginAuthViewModel(@NonNull Authenticator<CredType> authenticator) {
        super(authenticator);
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
