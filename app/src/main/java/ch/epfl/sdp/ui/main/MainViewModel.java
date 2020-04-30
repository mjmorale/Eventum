package ch.epfl.sdp.ui.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import ch.epfl.sdp.User;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.DocumentQuery;
import ch.epfl.sdp.ui.DatabaseViewModelFactory;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

/**
 * View model for the main activity
 */
public class MainViewModel extends ViewModel {

    /**
     * Factory for the MainViewModel
     */
    static class MainViewModelFactory extends DatabaseViewModelFactory {
        /**
         * Constructor of the MainViewModel factory
         */
        MainViewModelFactory() {
            super(String.class);
        }

        /**
         * Method to set a user to the MainViewModel factory
         *
         * @param userRef reference of the user
         */
        void setUserRef(@NonNull String userRef) {
            setValue(0, verifyNotNull(userRef));
        }
    }

    private DocumentQuery mUserDocument;
    private String mUserRef;

    private LiveData<User> mUserLiveData;

    /**
     * Constructor of the MainViewModel, the factory should be used instead of this
     *
     * @param userRef reference of the user
     * @param database {@link ch.epfl.sdp.db.Database}
     */
    public MainViewModel(@NonNull String userRef, @NonNull Database database) {
        mUserRef = userRef;
        mUserDocument = database.query("users").document(userRef);
    }

    /**
     * Method to get the user
     *
     * @return a live data of the user
     */
    public LiveData<User> getUser() {
        if(mUserLiveData == null) {
            mUserLiveData = mUserDocument.liveData(User.class);
        }
        return mUserLiveData;
    }

    /**
     * Method to get the reference of the user
     *
     * @return the user reference
     */
    public String getUserRef() {
        return mUserRef;
    }
}