package ch.epfl.sdp.offline;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.CDATASection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

/**
 * An object that loads images from an URL and caches it. If an image is already cached it will
 * use the cache. ImageGetter works as a singleton.
 */
public class ImageCache {

    private static ImageCache INSTANCE;

    private ImageCache() {}

    private static final String TAG = "ImageCache";

    private static HashMap<Date, String> cacheStatus;

    private static int MAX_CACHE_FILES = 20;

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

        File imageFile = new File(cacheDir, imageRef);

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
        cleanCache(cacheDir);
        File imageFile = new File(cacheDir, imageRef);
        try (FileOutputStream fileOutputStream = new FileOutputStream(imageFile)) {
            bitmap.compress(Bitmap.CompressFormat.WEBP, 100, fileOutputStream);
        } catch (Exception e) {
            Log.e(TAG, "Cannot save image in cache", e);
        }
    }

    private void cleanCache(File cacheDir){
        HashMap<Date, String> status = getCacheStatus(cacheDir);
        Set<Date> dateSet = status.keySet();

        // Implemented like this because in theory the number of files should not exceed MAX_CACHE_FILE_SIZE + 1
        while (cacheStatus.size() > MAX_CACHE_FILES){
            Date smallestDate = findSmallestInSet(dateSet);
            String toDelete = status.get(smallestDate);
            status.remove(smallestDate);
            File deleteFile = new File(cacheDir, toDelete);
            deleteFile.delete();
            dateSet = status.keySet();
        }
        updateCacheSummary(cacheDir);
        
    }

    private HashMap<Date, String> getCacheStatus(File cacheDir){
        if(cacheStatus == null){
            File statusFile = new File(cacheDir, TAG);
            try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(statusFile))){
                cacheStatus = (HashMap<Date, String>) objectInputStream.readObject();
            } catch (Exception e) {
                Log.e(TAG, "Cannot open ImageCacheStatusFile", e);
            }
        }
        return cacheStatus;
    }

    private Date findSmallestInSet(Set<Date> dates){
        Date returnValue;
        List<Date> list = new ArrayList<>();
        list.addAll(dates);

        int i = 0 ;
        returnValue = list.get(i);
        i++;

        for(;i < dates.size(); i++){
            Date d = list.get(i);
            if(d.compareTo(returnValue) > 0)
                returnValue = d;
        }
        return returnValue;
    }

    void updateCacheSummary(File cacheDir){
        File toUpdate = new File(cacheDir, TAG);
        toUpdate.delete();

        try(ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(toUpdate))){
            objectOutputStream.writeObject(cacheStatus);
        } catch (Exception e){
            Log.e(TAG, "Cannot update ImageCacheStatusFile", e);
        }
    }
}
