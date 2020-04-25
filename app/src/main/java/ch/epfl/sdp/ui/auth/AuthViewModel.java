package ch.epfl.sdp.ui.auth;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ch.epfl.sdp.User;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.auth.UserInfo;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.future.Future;
import ch.epfl.sdp.ui.DatabaseViewModelFactory;
import ch.epfl.sdp.ui.Event;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class AuthViewModel<CredType> extends ViewModel {

    static class AuthViewModelFactory extends DatabaseViewModelFactory {

        AuthViewModelFactory() {
            super(Authenticator.class);
        }

        void setAuthenticator(@NonNull Authenticator authenticator) {
            setValue(0, verifyNotNull(authenticator));
        }
    }

    private final static String TAG = "LoginAuthViewModel";

    private final Authenticator<CredType> mAuthenticator;
    private final MutableLiveData<String> mUserRef = new MutableLiveData<>();

    private final MutableLiveData<Event<? extends Exception>> mErrorEvent = new MutableLiveData<>();

    private final Database mDatabase;
    private final CollectionQuery mUserCollection;

    public AuthViewModel(@NonNull Authenticator<CredType> authenticator, @NonNull Database database) {
        mAuthenticator = verifyNotNull(authenticator);
        mDatabase = verifyNotNull(database);
        mUserCollection = mDatabase.query("users");

        UserInfo currentUser = mAuthenticator.getCurrentUserInfo();
        if(currentUser != null) {
            createUserInDatabase(currentUser);
        }
        else {
            mUserRef.postValue(null);
        }
    }

    public void login(CredType credential) {
        mAuthenticator.login(credential)
                .then(this::createUserInDatabase)
                .except(e -> {
                    mErrorEvent.postValue(new Event<>(e));
                    Log.e(TAG, "Cannot log in", e);
                });
    }

    @NonNull
    public LiveData<String> getUserRef() {
        return mUserRef;
    }

    @NonNull
    public LiveData<Event<? extends Exception>> getErrorEvent() {
        return mErrorEvent;
    }

    private void createUserInDatabase(@NonNull UserInfo userInfo) {
        verifyNotNull(userInfo);

        mUserCollection.document(userInfo.getUid()).exists()
                .then(exists -> {
                    if(exists) {
                        mUserRef.postValue(userInfo.getUid());
                    }
                    else {
                        User user = new User(userInfo.getDisplayName(), userInfo.getEmail());
                        mUserCollection.document(userInfo.getUid()).set(user)
                                .then(data -> mUserRef.postValue(userInfo.getUid()))
                                .except(e -> {
                                    mErrorEvent.postValue(new Event<>(e));
                                    Log.e(TAG, "Cannot create user in database", e);
                                });
                    }
                })
                .except(e -> {
                    mErrorEvent.postValue(new Event<>(e));
                    Log.e(TAG, "Cannot query the database", e);
                });
    }
}
