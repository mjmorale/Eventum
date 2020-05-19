package ch.epfl.sdp.ui.createevent;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.EventCategory;
import ch.epfl.sdp.R;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.auth.UserInfo;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.platforms.firebase.storage.FirestoreStorage;
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
    private final UserInfo mUserInfo;
    private String mImageId;
    private final ArrayList<EventCategory> mCategories = new ArrayList<EventCategory>();

    /**
     * Constructor of the CreateEventViewModel, the factory should be used instead of this
     *
     * @param storage where the images are uploaded
     * @param database where the events are uploaded
     * @param authenticator where the user is authenticated
     */
    public CreateEventViewModel(@NonNull Storage storage, @NonNull Authenticator authenticator, @NonNull Database database) {
        verifyNotNull(database);

        mStorage = verifyNotNull(storage);
        mUserInfo = authenticator.getCurrentUser();
        mEventCollection = database.query("events");
    }

    public String getUserRef() {
        return mUserInfo.getUid();
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

    /**
     * Method to check on a toggle button to add a category to an event
     *
     * @param viewId the id of the toggle button
     * @param view the current view
     * @param category the category to set
     */
    public void setCategoryFromToggleButton(View view, int viewId, EventCategory category) {
        ToggleButton toggle = (ToggleButton) view.findViewById(viewId);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mCategories.add(category);
                }
            }
        });
    }
}
