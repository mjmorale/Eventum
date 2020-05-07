package ch.epfl.sdp.platforms.firebase.storage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.FileOutputStream;

import ch.epfl.sdp.ObjectUtils;

/**
 * An object that loads images from an URL and caches it. If an image is already cached it will
 * use the cache. ImageGetter works as a singleton.
 */
public class ImageGetter {

    private static ImageGetter INSTANCE;

    private ImageGetter() {}

    /**
     * @return The instance of ImageGetter
     */
    public static ImageGetter getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ImageGetter();
        }
        return INSTANCE;
    }

    /**
     * Downloads an image and puts it into the imageView to display. Also saves it in cache
     * and loads it from there if possible
     *
     * @param context   The context in which the image should be displayed / loaded.
     * @param imageId   A link to an image, URL, Uri.. Should work with Glide.load()
     * @param imageView The ImageView that will hold the image.
     */
    public void getImage(@NonNull Context context, @NonNull Object imageId, @NonNull ImageView imageView) {
        ObjectUtils.verifyNotNull(context, imageId, imageView);

        int imageStringHash = imageId.hashCode();

        // This line is very practical when testing the cache with adb and avoids having
        // an ugly - in the cache file name
        String filename = Integer.toString(imageStringHash).replace('-', '1');

        File imageFile = new File(context.getCacheDir(), filename);

        Bitmap bitmap;

        if (imageFile.exists() && imageFile.isFile()) {
            bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            imageView.setImageBitmap(bitmap);
        } else {
            Glide.with(context).asBitmap().load(imageId).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    imageView.setImageBitmap(resource);
                    saveInCache(imageFile, resource);
                }
            });


        }

    }

    /**
     * Saves the bitmap in target file
     *
     * @param file   The file that will contain the bitmap
     * @param bitmap The bitmap to be written in the file
     */
    private static void saveInCache(File file, Bitmap bitmap) {

        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
