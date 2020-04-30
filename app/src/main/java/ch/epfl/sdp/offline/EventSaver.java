package ch.epfl.sdp.offline;

import android.content.Context;

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
import java.util.Objects;

public class EventSaver <Event> extends ObjectSaver {

    @Override
    protected String getCollectionString() {
        return "Events";
    }

    /**
     * Saves Event in a file
     * @param toSave The object to save in the collection
     * @param docReference Id of the document (Event)
     * @param deleteDate When we can delete the temp file
     */
    public void saveEvent(Event toSave, String docReference, Date deleteDate,Context context) throws IOException, ClassNotFoundException {
        HashMap<String, Date> statusFiles = getEventStatusFiles(context);
        saveFile((Serializable) toSave,docReference,deleteDate, context);

        //update status
        statusFiles.put(docReference, deleteDate);
        System.out.println(statusFiles);
        updateEventStatusFiles(statusFiles,context);

        //remove old files
        for (String key: statusFiles.keySet()) {
            if (Objects.requireNonNull(statusFiles.get(key)).before(new Date())){
                removeSingleEvent(key, context);
            }
        }
    }

    public List<Event> getAllEvents(Context context) throws IOException, ClassNotFoundException {
        HashMap<String, Date> statusFiles = getEventStatusFiles(context);
        List<String> listReference = new ArrayList<String>(statusFiles.keySet());
        System.out.println(listReference);
        return getMultipleFile(listReference,context);
    }

    public boolean removeSingleEvent(String docReference, Context context) throws IOException, ClassNotFoundException {
        removeSingleFile(docReference,context);
        HashMap<String, Date> statusFiles = getEventStatusFiles(context);
        Date elementRemoved = statusFiles.remove(docReference);
        updateEventStatusFiles(statusFiles,context);
        boolean elementIsRemoved = (elementRemoved != null);
        return elementIsRemoved;
    }

    private HashMap<String, Date> getEventStatusFiles(Context context) throws IOException, ClassNotFoundException {
        File statusFile = new File(context.getFilesDir(), "eventStatusFiles");
        if (!statusFile.exists()) return  new HashMap<String, Date>();;
        FileInputStream fi = new FileInputStream(statusFile);
        ObjectInputStream oi = new ObjectInputStream(fi);

        HashMap<String, Date> eventStatusFiles = (HashMap<String, Date>) oi.readObject();
        oi.close();
        fi.close();

        return eventStatusFiles;
    }

    private void  updateEventStatusFiles(HashMap<String, Date> eventStatusFiles, Context context) throws IOException {
        File statusFile = new File(context.getFilesDir(), "eventStatusFiles");
        FileOutputStream f = new FileOutputStream(statusFile);
        ObjectOutputStream o = new ObjectOutputStream((f));
        o.writeObject(eventStatusFiles);
        o.close();
        f.close();
    }
}
