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

/**
 * View model for the user settings
 */
public class SettingsViewModel extends ViewModel {

    /**
     * Factory for the SettingsViewModel
     */
    static class SettingsViewModelFactory extends DatabaseViewModelFactory {
        /**
         * Constructor of the SettingsViewModel factory
         */
        public SettingsViewModelFactory() {
            super(String.class, Authenticator.class);
        }

        /**
         * Method to set the user reference
         *
         * @param userRef the user reference
         */
        public void setUserRef(@NonNull String userRef) {
            setValue(0, verifyNotNull(userRef));
        }

        /**
         * Method to set the authenticator
         *
         * @param authenticator {@link ch.epfl.sdp.auth.Authenticator}
         */
        public void setAuthenticator(@NonNull Authenticator authenticator) {
            setValue(1, verifyNotNull(authenticator));
        }
    }

    private final Authenticator mAuthenticator;
    private final DocumentQuery mUserDocument;
    private LiveData<User> mUserLiveData;

    /**
     * Constructor of the CreateEventViewModel, the factory should be used instead of this
     *
     * @param userRef the user reference
     * @param authenticator {@link ch.epfl.sdp.auth.Authenticator}
     * @param database {@link ch.epfl.sdp.db.Database}
     */
    public SettingsViewModel(String userRef, Authenticator authenticator, Database database) {
        mUserDocument = database.query("users").document(userRef);
        mAuthenticator = authenticator;
    }

    /**
     * Method to get the user
     *
     * @return live data of the user
     */
    public LiveData<User> getUser() {
        if(mUserLiveData == null) {
            mUserLiveData = mUserDocument.livedata(User.class);
        }
        return mUserLiveData;
    }

    /**
     * Method to set the user name
     *
     * @param username of the user
     * @param callback called when the name is set
     */
    public void setUserName(@NonNull String username, @NonNull Query.OnQueryCompleteCallback<Void> callback) {
        verifyNotNull(username, callback);
        mUserDocument.update("username", username, callback);
    }

    /**
     * Method to delete the account
     *
     * @param callback called when deleted
     */
    public void deleteAccount(@NonNull Query.OnQueryCompleteCallback<Void> callback) {
        verifyNotNull(callback);
        mUserDocument.delete(callback);
    }

    /**
     * Method to logout the user
     */
    public void logout() {
        mAuthenticator.logout();
    }
}
