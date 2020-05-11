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
import ch.epfl.sdp.ui.ParameterizedViewModelFactory;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class UserProfileViewModel extends ViewModel {
    private final CollectionQuery mUserCollection;
    private LiveData<User> mUserLiveData;
    private String mCurrentUserId;

    public UserProfileViewModel(@NonNull String currentUserId, CollectionQuery userCollection) {
        mCurrentUserId=currentUserId;
        mUserCollection = userCollection;
    }

    public void updateImageId(String imageId) {
        mUserCollection.document(mCurrentUserId).update("imageId", imageId, result -> { });
    }

    public void updateDescription(String description) {
        mUserCollection.document(mCurrentUserId).update("description", description, result -> { });
    }

    public LiveData<User> getUserLive() {
        if (mUserLiveData == null) {
            mUserLiveData = mUserCollection.document(mCurrentUserId).liveData(User.class);
        }
        return mUserLiveData;
    }

    static class UserProfileViewModelFactory extends ParameterizedViewModelFactory {
        UserProfileViewModelFactory() {
            super(String.class, CollectionQuery.class);
        }

        void setCurrentUserId(@NonNull String currentUserId) {
            setValue(0, verifyNotNull(currentUserId));
        }
        void setUserCollection(@NonNull CollectionQuery userCollection) {
            setValue(1, verifyNotNull(userCollection));
        }
    }


}
