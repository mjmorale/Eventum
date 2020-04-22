package ch.epfl.sdp.ui.settings;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import ch.epfl.sdp.User;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.DocumentQuery;
import ch.epfl.sdp.db.queries.Query;
import ch.epfl.sdp.ui.DatabaseViewModelFactory;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class SettingsViewModel extends ViewModel {

    static class SettingsViewModelFactory extends DatabaseViewModelFactory {
        public SettingsViewModelFactory() {
            super(String.class, Authenticator.class);
        }

        public void setUserRef(@NonNull String userRef) {
            setValue(0, verifyNotNull(userRef));
        }

        public void setAuthenticator(@NonNull Authenticator authenticator) {
            setValue(1, verifyNotNull(authenticator));
        }
    }

    private final Authenticator mAuthenticator;
    private final DocumentQuery mUserDocument;
    private LiveData<User> mUserLiveData;

    public SettingsViewModel(String userRef, Authenticator authenticator, Database database) {
        mUserDocument = database.query("users").document(userRef);
        mAuthenticator = authenticator;
    }

    public LiveData<User> getUser() {
        if(mUserLiveData == null) {
            mUserLiveData = mUserDocument.livedata(User.class);
        }
        return mUserLiveData;
    }

    public void setUserName(@NonNull String username, @NonNull Query.OnQueryCompleteCallback<Void> callback) {
        verifyNotNull(username, callback);
        mUserDocument.update("username", username, callback);
    }

    public void deleteAccount(@NonNull Query.OnQueryCompleteCallback<Void> callback) {
        verifyNotNull(callback);
        mUserDocument.delete(callback);
    }

    public void logout() {
        mAuthenticator.logout();
    }
}
