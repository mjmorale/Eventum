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
import ch.epfl.sdp.db.queries.Query;
import ch.epfl.sdp.db.queries.QueryResult;
import ch.epfl.sdp.ui.ParameterizedViewModelFactory;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class AuthViewModel<CredType> extends ViewModel {

    static class AuthViewModelFactory extends ParameterizedViewModelFactory {

        AuthViewModelFactory() {
            super(Authenticator.class, Database.class);
        }

        void setAuthenticator(@NonNull Authenticator authenticator) {
            setValue(0, verifyNotNull(authenticator));
        }

        void setDatabase(@NonNull Database database) {
            setValue(1, verifyNotNull(database));
        }
    }

    private final static String TAG = "LoginAuthViewModel";

    private final Authenticator<CredType> mAuthenticator;
    private final MutableLiveData<String> mUserRef = new MutableLiveData<>();

    private final Database mDatabase;
    private final CollectionQuery mUserCollection;

    public AuthViewModel(@NonNull Authenticator<CredType> authenticator, @NonNull Database database) {
        mAuthenticator = verifyNotNull(authenticator);
        mDatabase = verifyNotNull(database);
        mUserCollection = mDatabase.query("users");

        UserInfo currentUser = mAuthenticator.getCurrentUser();
        if(currentUser != null) {
            createUserInDatabase(currentUser);
        }
        else {
            mUserRef.postValue(null);
        }
    }

    public void login(CredType credential) {
        mAuthenticator.login(credential, result -> {
            if(result.isSuccessful()) {
                createUserInDatabase(result.getUserInfo());
            }
            else { Log.e(TAG, "Cannot log in", result.getException());}
        });
    }

    @NonNull
    public LiveData<String> getUserRef() {
        return mUserRef;
    }

    private void createUserInDatabase(@NonNull UserInfo userInfo) {
        verifyNotNull(userInfo);

        mUserCollection.document(userInfo.getUid()).exists(existsResult -> {
            if(existsResult.isSuccessful()) {
                if(existsResult.getData()) {
                    // User exists in the database
                    // Post the user reference
                    mUserRef.postValue(userInfo.getUid());
                }
                else {
                    // User does not exist in the database
                    // Create the new user entry
                    User user = new User(userInfo.getDisplayName(), userInfo.getEmail());
                    mUserCollection.document(userInfo.getUid()).set(user, setResult -> {
                        if(setResult.isSuccessful()) {
                            mUserRef.postValue(userInfo.getUid());
                        }
                        else {
                            Log.e(TAG, "Cannot create user entry in database", setResult.getException());
                        }
                    });
                }
            }
            else {
                Log.e(TAG, "Cannot query the database", existsResult.getException());
            }
        });
    }
}
