package ch.epfl.sdp.ui;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import ch.epfl.sdp.storage.Storage;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

/**
 * ViewModel class that can display images into image views.
 * The source of these images is cached database.
 */
public class ImageViewModel extends ViewModel {

    /**
     *  Factory for the ImageViewModel
     */
    public static class ImageViewModelFactory extends ParameterizedViewModelFactory {
        /**
         * Create a new ImageViewModelFactory
         */
        public ImageViewModelFactory() {
            super(Storage.class, File.class);
        }

        /**
         * Set the storage interface of this factory
         * @param storage The storage interface instance
         */
        public void setStorage(@NonNull Storage storage) {
            setValue(0, verifyNotNull(storage));
        }

        /**
         * Set the image cache directory.
         * @param cacheDir The image cache directory.
         */
        public void setCacheDir(@NonNull File cacheDir) {
            setValue(1, verifyNotNull(cacheDir));
        }

        /**
         * @return The currently set cache dir value
         */
        public File getCacheDir() {
            return (File)getValue(1);
        }
    }

    private final static String TAG = "ImageViewModel";

    private final File mCacheDir;
    private final Storage mStorage;

    /**
     * Construct a new ImageViewModel
     * @param storage The storage interface to use
     * @param cacheDir The cache directory of the image cache
     */
    public ImageViewModel(@NonNull Storage storage, @NonNull File cacheDir) {
        mStorage = verifyNotNull(storage);
        mCacheDir = verifyNotNull(cacheDir);
    }

    /**
     * Load an image from the storage/cache into an imageview
     * @param folder The database folder in which the image resides
     * @param imageRef The image file name in the database folder
     * @param imageView The image view in which to display the output image
     */
    public void loadInto(@NonNull String folder, @NonNull String imageRef, @NonNull ImageView imageView) {
        verifyNotNull(folder, imageRef, imageView);
        mStorage.downloadImage(mCacheDir, folder, imageRef, 1, new Storage.BitmapReadyCallback() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);
            }

            @Override
            public void onFailure(Exception e) {
                Log.e(TAG, "Cannot download image from storage", e);
            }
        });
    }
}
