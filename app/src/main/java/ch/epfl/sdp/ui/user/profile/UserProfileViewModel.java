package ch.epfl.sdp.ui.user.profile;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import ch.epfl.sdp.User;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.auth.UserInfo;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.Query;
import ch.epfl.sdp.db.queries.QueryResult;
import ch.epfl.sdp.storage.Storage;
import ch.epfl.sdp.ui.DatabaseViewModelFactory;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

/**
 *View model typically used in User profile fragment
 */
public class UserProfileViewModel extends ViewModel {

    /**
     * The model factory used to construct a UserProfileViewModel
     */
    static class UserProfileViewModelFactory extends DatabaseViewModelFactory {

        UserProfileViewModelFactory() {
            super(Storage.class, Authenticator.class);
        }

        /**
         * @brief sets up the storage
         * @param storage used to upload pictures
         */
        void setStorage(@NonNull Storage storage) {
            setValue(0, verifyNotNull(storage));
        }

        /**
         * @brief sets up the authenticaator
         * @param authenticator to get the current user
         */
        void setAuthenticator(@NonNull Authenticator authenticator) {
            setValue(1, verifyNotNull(authenticator));
        }

    }

    private final CollectionQuery mUserCollection;
    private Storage mStorage;
    private UserInfo mUserInfo;
    private LiveData<User> mUserLiveData;

    /**
     * @Constructor should  only be used by the userProfileViewModelFactory
     * @param storage used to upload pictures
     * @param authenticator to get the current user
     * @param database to get the users collections
     */
    public UserProfileViewModel(@NonNull Storage storage, @NonNull Authenticator authenticator, @NonNull Database database) {
        verifyNotNull(database);
        mStorage=storage;
        mUserInfo = authenticator.getCurrentUser();
        mUserCollection = database.query("users");
    }

    /**
     * @brief updates the description of a user in the database
     * @param description to be added
     */
    public void updateDescription(String description) {
        mUserCollection.document(mUserInfo.getUid()).update("description", description, new Query.OnQueryCompleteCallback<Void>() {
            @Override
            public void onQueryComplete(QueryResult<Void> result) {

            }
        });
    }

    /**
     * @brief updated the imageId of user in the database
     * @param imageId to be added
     */
    public void updateImageId(String imageId) {
        mUserCollection.document(mUserInfo.getUid()).update("imageId", imageId, new Query.OnQueryCompleteCallback<Void>() {
            @Override
            public void onQueryComplete(QueryResult<Void> result) {

            }
        });
    }

    /**
     *
     * @return the current user live data
     */
    public LiveData<User> getUserLive() {
        if (mUserLiveData == null) {
            mUserLiveData = mUserCollection.document(mUserInfo.getUid()).liveData(User.class);
        }
        return mUserLiveData;
    }

    /**
     *
     * @return the storage used to upload pictures
     */
    public Storage getStorage(){
        return  mStorage;
    }


}