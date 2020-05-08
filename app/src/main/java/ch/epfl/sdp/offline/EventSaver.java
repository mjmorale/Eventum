package ch.epfl.sdp.offline;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

import ch.epfl.sdp.Event;
import ch.epfl.sdp.db.DatabaseObject;

public class EventSaver extends ObjectSaver<Event> {

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
    public void saveEvent(Event toSave, String docReference, Date deleteDate,File path) {
        HashMap<String, Map<String,Object>> statusFiles = getEventStatusFiles(path);

        Map<String,Object> metaData = new HashMap<String, Object>();
        Date lastModifyDate = new Date();
        metaData.put("deleteDate",deleteDate);
        metaData.put("lastModifyDate",lastModifyDate);

        saveFile(toSave, docReference, path);

        //update status
        statusFiles.put(docReference, metaData);
        updateEventStatusFiles(statusFiles,path);

        //remove old files
        for (String key: statusFiles.keySet()) {
            Date deleteDateStorage = (Date) Objects.requireNonNull(statusFiles.get(key)).get("deleteDate");
            assert deleteDateStorage != null;
            if (deleteDateStorage.before(new Date())){
                removeSingleEvent(key, path);
            }
        }
    }

    public List<Event> getAllEvents(File path) {
        HashMap<String, Map<String,Object>> statusFiles = getEventStatusFiles(path);
        List<String> listReference = new ArrayList<>(statusFiles.keySet());
        return getMultipleFile(listReference, path);
    }

    public List<DatabaseObject<Event>> getAllEventsWithRefs(File path) throws IOException, ClassNotFoundException {
        HashMap<String, Map<String,Object>> statusFiles = getEventStatusFiles(path);
        List<String> listReference = new ArrayList<>(statusFiles.keySet());
        List<Event> listEvents = getMultipleFile(listReference, path);
        List<DatabaseObject<Event>> listDatabaseObjects = new ArrayList<>();
        for(int i = 0; i < listReference.size(); i++) {
            listDatabaseObjects.add(new DatabaseObject<>(listReference.get(i), listEvents.get(i)));
        }
        return listDatabaseObjects;
    }

    public boolean removeSingleEvent(String docReference, File path) {
        removeSingleFile(docReference,path);
        HashMap<String, Map<String,Object>> statusFiles = getEventStatusFiles(path);
        Map<String, Object> elementRemoved = statusFiles.remove(docReference);
        updateEventStatusFiles(statusFiles,path);
        return (elementRemoved != null);
    }

    private HashMap<String, Map<String,Object>> getEventStatusFiles(File path) {
        File statusFile = new File(path, "eventStatusFiles");
        if (!statusFile.exists()) return  new HashMap<String, Map<String,Object>>();

        HashMap<String, Map<String,Object>> eventStatusFiles = null;

        try (FileInputStream fi = new FileInputStream(statusFile);ObjectInputStream oi = new ObjectInputStream(fi);){
            eventStatusFiles = (HashMap<String, Map<String,Object>>) oi.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return eventStatusFiles;
    }

    private void  updateEventStatusFiles(HashMap<String, Map<String,Object>> eventStatusFiles, File path) {
        File statusFile = new File(path, "eventStatusFiles");
        try(FileOutputStream f = new FileOutputStream(statusFile);ObjectOutputStream o = new ObjectOutputStream((f));){
            o.writeObject(eventStatusFiles);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
