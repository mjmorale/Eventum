package ch.epfl.sdp.platforms.firebase.storage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.UUID;

import ch.epfl.sdp.offline.ImageCache;
import ch.epfl.sdp.storage.Storage;
import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

/**
 * Class that interfaces with the Firestore storage API to manage files in the cloud.
 */
public class FirestoreStorage implements Storage {

    private final FirebaseStorage mStorage;
    private final ImageCache mImageCache;

    /**
     * Create a new FirestoreStorage
     *
     * @param firebase The instance of the underlying firestore storage API.
     */
    public FirestoreStorage(@NonNull FirebaseStorage firebase, @NonNull ImageCache imageCache) {
        mStorage = verifyNotNull(firebase);
        mImageCache = verifyNotNull(imageCache);
    }

    @Override
    public void uploadImage(@NonNull String folder, @NonNull Bitmap image, int compression, @NonNull RefReadyCallback callback) {
        verifyNotNull(folder, image, callback);
        if(compression < 0 || compression > 100) {
            throw new IllegalArgumentException("Compression factor is between 0 and 100");
        }
        // Generate a random image identifier
        String imageUUID = UUID.randomUUID().toString();
        // Compress the bitmap image in the WebP format for best compression quality
        ByteArrayOutputStream imageBytes = new ByteArrayOutputStream();
        if(!image.compress(Bitmap.CompressFormat.WEBP, compression, imageBytes)) {
            // Compression failed
            callback.onFailure(new Exception("Cannot compress given bitmap"));
        }
        else {
            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setContentType("image/webp")
                    .build();
            // Upload the image to Firebase storage.
            mStorage.getReference()
                    .child(folder)
                    .child(imageUUID)
                    .putBytes(imageBytes.toByteArray(), metadata)
                    .addOnSuccessListener(task -> callback.onSuccess(imageUUID))
                    .addOnFailureListener(callback::onFailure);
        }
    }

    @Override
    public void downloadImage(@NonNull File cacheDir, @NonNull String folder, @NonNull String imageRef, int maxSizeMb, @NonNull BitmapReadyCallback callback) {
        verifyNotNull(cacheDir, folder, imageRef, callback);
        if(maxSizeMb <= 0) {
            throw new IllegalArgumentException("File size should be greater than one");
        }
        Bitmap cachedImage = mImageCache.getImage(cacheDir, imageRef);
        if(cachedImage == null) {
            // Cache does not contain image.
            long downloadSize = maxSizeMb * 1024 * 1024; // Download size in megabytes
            mStorage.getReference()
                    .child(folder)
                    .child(imageRef)
                    .getBytes(downloadSize)
                    .addOnSuccessListener(bytes -> {
                        Bitmap downloadedImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        mImageCache.saveImage(cacheDir, imageRef, downloadedImage);
                        callback.onSuccess(downloadedImage);
                    })
                    .addOnFailureListener(callback::onFailure);
        }
        else {
            // Cache contains image.
            callback.onSuccess(cachedImage);
        }
    }

}
