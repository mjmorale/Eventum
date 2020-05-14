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

public class UserProfileViewModel extends ViewModel {

    static class MyViewModelFactory extends DatabaseViewModelFactory {

        MyViewModelFactory() {
            super(Storage.class, Authenticator.class);
        }

        void setStorage(@NonNull Storage storage) {
            setValue(0, verifyNotNull(storage));
        }

        void setAuthenticator(@NonNull Authenticator authenticator) {
            setValue(1, verifyNotNull(authenticator));
        }

    }

    private final CollectionQuery mUserCollection;
    private Storage mStorage;
    private UserInfo mUserInfo;
    private LiveData<User> mUserLiveData;

    public UserProfileViewModel(@NonNull Storage storage, @NonNull Authenticator authenticator, @NonNull Database database) {
        verifyNotNull(database);
        mStorage=storage;
        mUserInfo = authenticator.getCurrentUser();
        mUserCollection = database.query("users");
    }

    public void updateDescription(String description) {
        mUserCollection.document(mUserInfo.getUid()).update("description", description, new Query.OnQueryCompleteCallback<Void>() {
            @Override
            public void onQueryComplete(QueryResult<Void> result) {

            }
        });
    }

    public void updateImageId(String imageId) {
        mUserCollection.document(mUserInfo.getUid()).update("imageId", imageId, new Query.OnQueryCompleteCallback<Void>() {
            @Override
            public void onQueryComplete(QueryResult<Void> result) {

            }
        });
    }

    public LiveData<User> getUserLive() {
        if (mUserLiveData == null) {
            mUserLiveData = mUserCollection.document(mUserInfo.getUid()).liveData(User.class);
        }
        return mUserLiveData;
    }

    public Storage getStorage(){
        return  mStorage;
    }


}