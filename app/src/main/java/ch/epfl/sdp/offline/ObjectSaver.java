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
public abstract class ObjectSaver <T extends Serializable> {

    static private HashMap<String, Date> statusFiles = new HashMap<>();

    /**
     * Saves an object in a file
     *
     * @param toSave The object to save in the collection
     * @param docReference Id of the document
     * @param path local path where file is located
     */
    public void saveFile(T toSave, String docReference,File path) {

        File newFile = new File(path, docReference);

        if (newFile.exists()) {
            if(newFile.delete()) Log.d("Info Temp File", "Tempory file"+docReference+"deleted");
        }

        //save file
        try (FileOutputStream f = new FileOutputStream(newFile); ObjectOutputStream o = new ObjectOutputStream((f))) {
            o.writeObject(toSave);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Query multiple local files based on a list reference and a path.
     *
     * @param listReference list of id from files you want to get
     * @param path local path where file is located
     * @return list of object contained in the specified path
     */
    public List<T> getMultipleFile(List<String> listReference, File path)  {
        List<T> result = new ArrayList<T>();

        for (String docReference : listReference) {
            File fileDescriptor = new File(path,docReference);

            try (FileInputStream fi = new FileInputStream(fileDescriptor); ObjectInputStream oi = new ObjectInputStream(fi)) {

                T tempRead = (T) oi.readObject();
                result.add(tempRead);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * Query a single local file based on a document reference and a path.
     *
     * @param docReference Id of the document
     * @param path local path where file is located
     * @return object contained in the specified path
     */
    public T getSingleFile(String docReference, File path) {
        File fileDescriptor = new File(path,docReference);

        T tempRead = null;
        try (FileInputStream fi = new FileInputStream(fileDescriptor); ObjectInputStream oi = new ObjectInputStream(fi);){
            tempRead = (T) oi.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return tempRead;
    }

    /**
     * Remove a single local file based on a document reference and a path.
     *
     * @param path local path where file to remove is located
     * @param docReference Id of the document
     */
    public void removeSingleFile(String docReference, File path) {
        File fileDescriptor = new File(path,docReference);
        fileDescriptor.delete();
    }

    @NonNull
    protected abstract String getCollectionString();
}
