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
import java.util.Map;
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
    public void saveEvent(Event toSave, String docReference, Date deleteDate,File path) throws IOException, ClassNotFoundException {
        HashMap<String, Map<String,Object>> statusFiles = getEventStatusFiles(path);

        Map<String,Object> metaData = new HashMap<String, Object>();
        Date lastModifyDate = new Date();
        metaData.put("deleteDate",deleteDate);
        metaData.put("lastModifyDate",lastModifyDate);

        saveFile((Serializable) toSave,docReference, path);

        //update status
        statusFiles.put(docReference, metaData);
        updateEventStatusFiles(statusFiles,path);

        //remove old files
        for (String key: statusFiles.keySet()) {
            Date deleteDateStorage = (Date) statusFiles.get(key).get("deleteDate");
            if (deleteDateStorage.before(new Date())){
                removeSingleEvent(key, path);
            }
        }
    }

    public List<Event> getAllEvents(File path) throws IOException, ClassNotFoundException {
        HashMap<String, Map<String,Object>> statusFiles = getEventStatusFiles(path);
        List<String> listReference = new ArrayList<String>(statusFiles.keySet());
        return getMultipleFile(listReference,path);
    }

    public boolean removeSingleEvent(String docReference, File path) throws IOException, ClassNotFoundException {
        removeSingleFile(docReference,path);
        HashMap<String, Map<String,Object>> statusFiles = getEventStatusFiles(path);
        Map<String, Object> elementRemoved = statusFiles.remove(docReference);
        updateEventStatusFiles(statusFiles,path);
        boolean elementIsRemoved = (elementRemoved != null);
        return elementIsRemoved;
    }

    private HashMap<String, Map<String,Object>> getEventStatusFiles(File path) throws IOException, ClassNotFoundException {
        File statusFile = new File(path, "eventStatusFiles");
        if (!statusFile.exists()) return  new HashMap<String, Map<String,Object>>();;
        FileInputStream fi = new FileInputStream(statusFile);
        ObjectInputStream oi = new ObjectInputStream(fi);

        HashMap<String, Map<String,Object>> eventStatusFiles = (HashMap<String, Map<String,Object>>) oi.readObject();
        oi.close();
        fi.close();

        return eventStatusFiles;
    }

    private void  updateEventStatusFiles(HashMap<String, Map<String,Object>> eventStatusFiles, File path) throws IOException {
        File statusFile = new File(path, "eventStatusFiles");
        FileOutputStream f = new FileOutputStream(statusFile);
        ObjectOutputStream o = new ObjectOutputStream((f));
        o.writeObject(eventStatusFiles);
        o.close();
        f.close();
    }
}
