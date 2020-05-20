package ch.epfl.sdp.offline;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

/**
 * An object that loads images from an URL and caches it. If an image is already cached it will
 * use the cache. ImageGetter works as a singleton.
 */
public class ImageCache {

    private static ImageCache INSTANCE;

    private ImageCache() {}

    private static final String TAG = "ImageGetter";

    /**
     * @return The instance of ImageGetter
     */
    public static ImageCache getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ImageCache();
        }
        return INSTANCE;
    }

    /**
     * Go and retrieve an image from the cache.
     *
     * @param cacheDir The cache directory.
     * @param imageRef The image reference string.
     * @return The bitmap loaded from the cache or null if the image is not in the cache.
     */
    @Nullable
    public Bitmap getImage(@NonNull File cacheDir, @NonNull String imageRef) {
        verifyNotNull(cacheDir, imageRef);

        // This line is very practical when testing the cache with adb and avoids having
        // an ugly '-' in the cache file name
        String filename = imageRef.replace('-', '1');

        File imageFile = new File(cacheDir, filename);

        if (imageFile.exists() && imageFile.isFile()) {
            return BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        }
        return null;
    }

    /**
     * Saves the bitmap in the cache
     *
     * @param cacheDir The cache directory in which to save the image
     * @param bitmap The bitmap to be written in the file
     * @param imageRef The image unique reference
     */
    public void saveImage(@NonNull File cacheDir, @NonNull String imageRef, @NonNull Bitmap bitmap) {
        verifyNotNull(cacheDir, imageRef, bitmap);
        File imageFile = new File(cacheDir, imageRef);
        try (FileOutputStream fileOutputStream = new FileOutputStream(imageFile)) {
            bitmap.compress(Bitmap.CompressFormat.WEBP, 100, fileOutputStream);
        } catch (Exception e) {
            Log.e(TAG, "Cannot save image in cache", e);
        }
    }
}
