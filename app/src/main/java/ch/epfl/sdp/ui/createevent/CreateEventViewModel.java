package ch.epfl.sdp.ui.createevent;

import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import ch.epfl.sdp.Event;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.platforms.firebase.storage.FirestoreStorage;
import ch.epfl.sdp.storage.Storage;
import ch.epfl.sdp.ui.DatabaseViewModelFactory;
import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

/**
 * View model used for the creation of a new event
 */
public class CreateEventViewModel extends ViewModel {

    /**
     * Factory for the CreateEventViewModel
     */
    static class CreateEventViewModelFactory extends DatabaseViewModelFactory {
        /**
         * Constructor of the CreateEventViewModel factory
         */
        CreateEventViewModelFactory() {
            super(Storage.class);
        }

        /**
         * Method to set the storage to the CreateEventViewModel factory
         *
         * @param storage where the images are uploaded
         */
        void setStorage(@NonNull Storage storage) {
            setValue(0, verifyNotNull(storage));
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

    /**
     * Constructor of the CreateEventViewModel, the factory should be used instead of this
     *
     * @param storage where the images are uploaded
     * @param database where the events are uploaded
     */
    public CreateEventViewModel(@NonNull Storage storage, @NonNull Database database) {
        mDatabase = verifyNotNull(database);
        mStorage = verifyNotNull(storage);
        mEventCollection = mDatabase.query("events");
    }

    /**
     * Method to insert an event in the database
     *
     * @param event to be inserted
     * @param callback called when the upload is done (on failure or on success)
     */
    public void insertEvent(@NonNull Event event, @NonNull OnEventCreatedCallback callback) {
        mEventCollection.create(event, res -> {
            if(res.isSuccessful()) {
                callback.onSuccess(res.getData());
            } else {
                callback.onFailure(res.getException());
            }
        });
    }

    /**
     * Method to upload an image on the storage
     *
     * @param imageUri of the image to be uploaded
     */
    public void uploadImage(@NonNull Uri imageUri) {
        mStorage.uploadImage(imageUri, new FirestoreStorage.UrlReadyCallback() {
            @Override
            public void onSuccess(String url) { mImageId = url; }

            @Override
            public void onFailure() { mImageId = null; }
        });
    }

    /**
     * Method to get the image id of the event
     *
     * @return the image id of the event (URL)
     */
    public String getImageId() {
        return mImageId;
    }
}
