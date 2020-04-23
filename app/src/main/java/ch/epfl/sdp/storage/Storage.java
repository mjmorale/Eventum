package ch.epfl.sdp.storage;

import android.net.Uri;

import androidx.annotation.NonNull;
import ch.epfl.sdp.platforms.firebase.storage.FirestoreStorage;

/**
 * Interface for the FirestoreStorage.
 */
public interface Storage {
    void uploadImage(@NonNull Uri imageUri, @NonNull FirestoreStorage.UrlReadyCallback callback);
}
