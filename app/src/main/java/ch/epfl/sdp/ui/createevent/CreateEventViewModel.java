package ch.epfl.sdp.ui.createevent;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;
import ch.epfl.sdp.Event;
import ch.epfl.sdp.EventBuilder;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.storage.Storage;
import ch.epfl.sdp.ui.DatabaseViewModelFactory;
import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

/**
 * View model for the creation of a new event
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
            super(Storage.class, Authenticator.class);
        }

        /**
         * Method to set the storage to the CreateEventViewModel factory
         *
         * @param storage where the images are uploaded
         */
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
    private final String mUserRef;

    /**
     * Constructor of the CreateEventViewModel, the factory should be used instead of this
     *
     * @param storage where the images are uploaded
     * @param database where the events are uploaded
     * @param authenticator where the user is authenticated
     */
    public CreateEventViewModel(@NonNull Storage storage, @NonNull Authenticator authenticator, @NonNull Database database) {
        verifyNotNull(database, authenticator, storage);
        mStorage = storage;
        mUserRef = authenticator.getCurrentUser().getUid();
        mEventCollection = database.query("events");
    }

    /**
     * Method to insert an event in the database
     *
     * @param eventBuilder Partially built event. The organizer ref and image ref will be inserted
     *                     in the viewmodel.
     * @param image The bitmap image that represents the event or null if the default image is used.
     * @param callback called when the upload is done (on failure or on success)
     */
    public void insertEvent(@NonNull EventBuilder eventBuilder, @Nullable Bitmap image, @NonNull OnEventCreatedCallback callback) {
        eventBuilder.setOrganizerRef(mUserRef);
        if(image != null) {
            mStorage.uploadImage("events", image, 80, new Storage.RefReadyCallback() {
                @Override
                public void onSuccess(String ref) {
                    eventBuilder.setImageId(ref);
                    uploadCompleteEvent(eventBuilder.build(), callback);
                }

                @Override
                public void onFailure(Exception e) {
                    callback.onFailure(e);
                }
            });
        }
        else {
            eventBuilder.setImageId("EventDefaultImage.jpg");
            uploadCompleteEvent(eventBuilder.build(), callback);
        }
    }

    private void uploadCompleteEvent(@NonNull Event event, @NonNull OnEventCreatedCallback callback) {
        mEventCollection.create(event, res -> {
            if(res.isSuccessful()) {
                callback.onSuccess(res.getData());
            } else {
                callback.onFailure(res.getException());
            }
        });
    }
}
