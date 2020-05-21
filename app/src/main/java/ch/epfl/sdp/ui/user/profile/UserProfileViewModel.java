package ch.epfl.sdp.ui.user.profile;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import ch.epfl.sdp.User;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.auth.UserInfo;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.platforms.firebase.storage.FirestoreStorage;
import ch.epfl.sdp.platforms.firebase.storage.ImageGetter;
import ch.epfl.sdp.storage.Storage;
import ch.epfl.sdp.ui.DatabaseViewModelFactory;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

/**
 * View model typically used in User profile fragment
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
         * @param storage used to upload pictures
         * @brief sets up the storage
         */
        void setStorage(@NonNull Storage storage) {
            setValue(0, verifyNotNull(storage));
        }

        /**
         * @param authenticator to get the current user
         * @brief sets up the authenticaator
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
     * @param storage       used to upload pictures
     * @param authenticator to get the current user
     * @param database      to get the users collections
     * @Constructor should  only be used by the userProfileViewModelFactory
     */
    public UserProfileViewModel(@NonNull Storage storage, @NonNull Authenticator authenticator, @NonNull Database database) {
        verifyNotNull(database, authenticator);
        mStorage = verifyNotNull(storage);
        mUserInfo = authenticator.getCurrentUser();
        mUserCollection = database.query("users");
    }

    /**
     * @param description to be added
     * @brief updates the description of a user in the si je fais livedata.getValue().getId() ca va fail bien evidement. database
     */
    public void updateDescription(String description) {
        mUserCollection.document(mUserInfo.getUid()).update("description", description, result -> {
        });
    }

    /**
     * @param imageId to be added
     * @brief updated the imageId of user in the database
     */
    public void updateImageId(String imageId) {
        mUserCollection.document(mUserInfo.getUid()).update("imageId", imageId, result -> {
            if (!result.isSuccessful())
                System.err.println(result.getException().getMessage());
        });
    }

    /**
     * @return the current user live data
     */
    public LiveData<User> getUserLive() {
        if (mUserLiveData == null) {
            mUserLiveData = mUserCollection.document(mUserInfo.getUid()).liveData(User.class);
        }
        return mUserLiveData;
    }

    /**
     * @param imageUri       of the image to be set
     * @param context        of the app
     * @param imageView      where the image to be set
     * @param uploadCallBack after the upload
     * @set an image into an image view
     */
    public void setImage(Uri imageUri, Context context, ImageView imageView, FirestoreStorage.UrlReadyCallback uploadCallBack) {
        ImageGetter.getInstance().getImage(context, imageUri, imageView);
        imageView.setTag("new_image");
        updateImageId(imageUri.toString());
        mStorage.uploadImage(imageUri, uploadCallBack);
    }

}