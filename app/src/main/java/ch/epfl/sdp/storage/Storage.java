package ch.epfl.sdp.storage;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;

import androidx.annotation.NonNull;
import ch.epfl.sdp.platforms.firebase.storage.FirestoreStorage;

/**
 * Interface for the FirestoreStorage.
 * To store an image on firebase storage from an image uri.
 */
public interface Storage {

    interface RefReadyCallback {
        void onSuccess(String ref);
        void onFailure(Exception e);
    }

    interface BitmapReadyCallback {
        void onSuccess(Bitmap bitmap);
        void onFailure(Exception e);
    }

    /**
     * Method to upload an image to the storage
     *
     * @param folder The folder name to store the file into
     * @param image The image data to upload
     * @param compression The quality of compression (best 100 - worst 0)
     * @param callback The callback method to call when the operation is finished
     */
    void uploadImage(@NonNull String folder, @NonNull Bitmap image, int compression, @NonNull RefReadyCallback callback);

    /**
     * Download an image file from the storage.
     *
     * @param cacheDir The cache directory in which to read and write images
     * @param folder The folder name where the target image is located
     * @param imageRef The image name to download
     * @param maxSizeMb The maximum file size in megabytes
     * @param callback The callback method to call when the operation is finished
     */
    void downloadImage(@NonNull File cacheDir, @NonNull String folder, @NonNull String imageRef, int maxSizeMb, @NonNull BitmapReadyCallback callback);
}
