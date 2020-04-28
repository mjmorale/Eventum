package ch.epfl.sdp.offline;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

/**
 * Classes implementing this abstract class allows to save an Object in the app-specific storage
 * It works with the database to keep the local copy up to date
 */
public abstract class ObjectSaver <T extends Serializable> implements OfflineDatabaseSaver {

    private Dictionary<String, Date> statusFiles = new Hashtable<String, Date>();

    /**
     * Saves an object in a file
     * @param deleteDate When we can delete the temp file
     * @param toSave The object to save in the collection
     * @param docReference
     */
    public void saveFile(T toSave, String docReference, Date deleteDate) throws IOException {
        File newFile = File.createTempFile(docReference, null);

        //delete old version if exist
        if (newFile.exists()) {
            if(newFile.delete()) Log.d("Info Temp File", "Tempory file"+docReference+"deleted");
        }

        //save file
        FileOutputStream f = new FileOutputStream(newFile);
        ObjectOutputStream o = new ObjectOutputStream((f));
        o.writeObject(toSave);
        o.close();
        f.close();

        //update status  of the cache
        statusFiles.put(docReference, deleteDate);

        //check all status of the cache

    }

    /**
     *
     * @param docReference
     * @return
     */
    public List<T> getMultipleFile(List<String> docReference){

        return null;
    }

    /**
     *
     * @param docReference
     * @return
     */
    public T getSingleFile(String docReference){
        return null;
    }

    @NonNull
    protected abstract String getCollectionString();
}
