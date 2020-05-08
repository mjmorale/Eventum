package ch.epfl.sdp.ui.user.profile;

import android.net.Uri;

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
import ch.epfl.sdp.platforms.firebase.storage.FirestoreStorage;
import ch.epfl.sdp.storage.Storage;
import ch.epfl.sdp.ui.DatabaseViewModelFactory;
import ch.epfl.sdp.ui.ParameterizedViewModelFactory;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class UserProfileViewModel extends ViewModel {
    private  Storage mStorage;
    private  UserInfo mUserInfo;
    private String mImageId;
    private final CollectionQuery mUserCollection;
    private LiveData<User> mUserLiveData;

    static class MyViewModelFactory extends DatabaseViewModelFactory{

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

    public UserProfileViewModel(@NonNull Storage storage, @NonNull Authenticator authenticator, @NonNull Database database) {
        verifyNotNull(database);
        mStorage = verifyNotNull(storage);
        mUserInfo = authenticator.getCurrentUser();
        mUserCollection = database.query("users");
    }


    public void uploadImage(@NonNull Uri imageUri) {
        mStorage.uploadImage(imageUri, new FirestoreStorage.UrlReadyCallback() {
            @Override
            public void onSuccess(String url) {
                mImageId = url;
                mUserCollection.document(mUserInfo.getUid()).update("imageId", mImageId, new Query.OnQueryCompleteCallback<Void>() {
                    @Override
                    public void onQueryComplete(QueryResult<Void> result) {

                    }
                });
            }

            @Override
            public void onFailure() { mImageId = null; }
        });
    }



    public void updateDescription(String description){
        mUserCollection.document(mUserInfo.getUid()).update("description", description, new Query.OnQueryCompleteCallback<Void>() {
            @Override
            public void onQueryComplete(QueryResult<Void> result) {

            }
        });
    }
    public LiveData<User> getUserLive(){
        if(mUserLiveData==null){
            mUserLiveData=mUserCollection.document(mUserInfo.getUid()).liveData(User.class);
        }
        return mUserLiveData;
    }


}
