package ch.epfl.sdp.ui.createevent;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.UploadTask;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.auth.UserInfo;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.platforms.firebase.storage.FirestoreStorage;
import ch.epfl.sdp.storage.Storage;
import ch.epfl.sdp.ui.DatabaseViewModelFactory;
import ch.epfl.sdp.ui.ParameterizedViewModelFactory;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class CreateEventViewModel extends ViewModel {

    static class CreateEventViewModelFactory extends DatabaseViewModelFactory {
        CreateEventViewModelFactory() {
            super(Storage.class, Authenticator.class);
        }

        void setStorage(@NonNull Storage storage) {
            setValue(0, verifyNotNull(storage));
        }

        void setAuthenticator(@NonNull Authenticator authenticator) {
            setValue(1, verifyNotNull(authenticator));
        }
    }

    interface OnEventCreatedCallback {
        void onSuccess(String eventRef);
        void onFailure(Exception exception);
    }

    private final CollectionQuery mEventCollection;
    private final Storage mStorage;
    private final UserInfo mUserInfo;
    private String mImageId;

    public CreateEventViewModel(@NonNull Storage storage, @NonNull Authenticator authenticator, @NonNull Database database) {
        verifyNotNull(database);
        mStorage = verifyNotNull(storage);
        mUserInfo = authenticator.getCurrentUser();
        mEventCollection = database.query("events");
    }

    public String getUserRef() {
        return mUserInfo.getUid();
    }

    public void insertEvent(@NonNull Event event, @NonNull OnEventCreatedCallback callback) {
        mEventCollection.create(event, res -> {
            if(res.isSuccessful()) {
                callback.onSuccess(res.getData());
            } else {
                callback.onFailure(res.getException());
            }
        });
    }

    public void uploadImage(@NonNull Uri imageUri) {
        mStorage.uploadImage(imageUri, new FirestoreStorage.UrlReadyCallback() {
            @Override
            public void onSuccess(String url) { mImageId = url; }

            @Override
            public void onFailure() { mImageId = null; }
        });
    }

    public String getImageId() {
        return mImageId;
    }
}
