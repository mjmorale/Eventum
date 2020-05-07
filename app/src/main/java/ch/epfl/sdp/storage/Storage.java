package ch.epfl.sdp.storage;

import android.net.Uri;

import androidx.annotation.NonNull;

import ch.epfl.sdp.platforms.firebase.storage.FirestoreStorage;

/**
 * Interface for the FirestoreStorage.
 * To store an image on firebase storage from an image uri.
 */
public interface Storage {
    /**
     * Method to upload an image to the storage
     *
     * @param imageUri of the image to be uploaded
     * @param callback called when the upload is finished
     */
    void uploadImage(@NonNull Uri imageUri, @NonNull FirestoreStorage.UrlReadyCallback callback);


}
