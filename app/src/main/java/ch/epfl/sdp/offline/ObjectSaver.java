package ch.epfl.sdp.offline;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.Serializable;
import java.util.List;

/**
 * Classes implementing this abstract class allows to save an Object in the app-specific storage
 * It works with the database to keep the local copy up to date
 */
public abstract class ObjectSaver <T extends Serializable> implements OfflineDatabaseSaver {

    /**
     * Saves an object in a file
     * @param context The context
     * @param toSave The object to save in the collection
     * @param docReference
     */
    public void saveFile(Context context, T toSave, String docReference){
        File root = context.getFilesDir();
        File collection = new File(root, getCollectionString());
        if(collection.exists() && collection.isDirectory()){

        } else {
            Log.d("Collection missing", "Could not find " + getCollectionString());
        }
    }

    /**
     *
     * @param context
     * @param docReference
     * @return
     */
    public List<T> getMultipleFile(Context context, List<String> docReference){

        return null;
    }

    /**
     *
     * @param context
     * @param docReference
     * @return
     */
    public T getSingleFile(Context context, String docReference){
        return null;
    }

    @NonNull
    protected abstract String getCollectionString();
}
