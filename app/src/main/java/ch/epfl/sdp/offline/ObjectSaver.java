package ch.epfl.sdp.offline;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Classes implementing this abstract class allows to save an Object in the app-specific storage
 * It works with the database to keep the local copy up to date
 */
public abstract class ObjectSaver <T extends Serializable> implements OfflineDatabaseSaver {

    static private HashMap<String, Date> statusFiles = new HashMap<>();

    /**
     * Saves an object in a file
     * @param toSave The object to save in the collection
     * @param docReference Id of the document
     */
    public void saveFile(T toSave, String docReference, File path) throws IOException {
        File newFile = new File(path, docReference);

        if (newFile.exists()) {
            if(newFile.delete()) Log.d("Info Temp File", "Tempory file"+docReference+"deleted");
        }

        //save file
        FileOutputStream f = new FileOutputStream(newFile);
        ObjectOutputStream o = new ObjectOutputStream((f));
        o.writeObject(toSave);
        o.close();
        f.close();
    }

    /**
     * @param listReference list of id from files you want to get
     * @return
     */
    public List<T> getMultipleFile(List<String> listReference, File path) throws IOException, ClassNotFoundException {
        List<T> result = new ArrayList<T>();

        for (String docReference : listReference) {
            File fileDescriptor = new File(path,docReference);
            FileInputStream fi = new FileInputStream(fileDescriptor);
            ObjectInputStream oi = new ObjectInputStream(fi);

            T tempRead = (T) oi.readObject();
            result.add(tempRead);

            oi.close();
            fi.close();
        }
        return result;
    }

    /**
     * @param docReference Id of the document
     * @return
     */
    public T getSingleFile(String docReference, File path) throws IOException, ClassNotFoundException {
        File fileDescriptor = new File(path, docReference);
        FileInputStream fi = new FileInputStream(fileDescriptor);
        ObjectInputStream oi = new ObjectInputStream(fi);

        T tempRead = (T) oi.readObject();
        oi.close();
        fi.close();

        return tempRead;
    }

    public void removeSingleFile(String docReference, File path) {
        File fileDescriptor = new File(path, docReference);
        fileDescriptor.delete();
    }

    @NonNull
    protected abstract String getCollectionString();
}
