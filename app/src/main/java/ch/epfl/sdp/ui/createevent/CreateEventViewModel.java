package ch.epfl.sdp.ui.createevent;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.UploadTask;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.platforms.firebase.storage.FirestoreStorage;
import ch.epfl.sdp.storage.Storage;
import ch.epfl.sdp.ui.DatabaseViewModelFactory;
import ch.epfl.sdp.ui.ParameterizedViewModelFactory;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class CreateEventViewModel extends ViewModel {

    static class CreateEventViewModelFactory extends ParameterizedViewModelFactory {
        CreateEventViewModelFactory() {
            super(Database.class, Storage.class);
        }
        void setDatabase(@NonNull Database database) {
            setValue(0, verifyNotNull(database));
        }
        void setStorage(@NonNull Storage storage) {
            setValue(1, verifyNotNull(storage));
        }
    }

    interface OnEventCreatedCallback {
        void onSuccess(String eventRef);
        void onFailure(Exception exception);
    }

    private final CollectionQuery mEventCollection;
    private final Database mDatabase;
    private final Storage mStorage;
    private String mImageId;

    public CreateEventViewModel(@NonNull Database database, @NonNull Storage storage) {
        mDatabase = verifyNotNull(database);
        mStorage = verifyNotNull(storage);
        mEventCollection = mDatabase.query("events");
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
            public void onSuccess(String url) {
                mImageId = url;
            }
        });
    }

    public String getImageId() {
        return mImageId;
    }
}
