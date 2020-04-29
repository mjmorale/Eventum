package ch.epfl.sdp.offline;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class EventSaver <Event> extends ObjectSaver {

    static private HashMap<String, Date> statusFiles = new HashMap<String, Date>();

    @Override
    protected String getCollectionString() {
        return "Events";
    }

    /**
     * Saves an object in a file
     * @param toSave The object to save in the collection
     * @param docReference Id of the document (Event)
     * @param deleteDate When we can delete the temp file
     */
    public void saveEvent(Event toSave, String docReference, Date deleteDate,Context context) throws IOException {
        if (deleteDate.after(new Date())){
            saveFile((Serializable) toSave,docReference,deleteDate, context);

            //update status
            statusFiles.put(docReference, deleteDate);

            //remove old files
            for (String key: statusFiles.keySet()) {
                if (Objects.requireNonNull(statusFiles.get(key)).before(new Date())){
                    //delete the file and remove from status
                    try {
                        removeSingleFile(key, context);
                    } catch (Exception exc){
                        exc.printStackTrace();
                    }
                }
            }
        }
        else{
            Log.e("EventSaver", "Date of the event is in the past");
        }
    }

    public List<Event> getAllEvents(Context context) throws IOException, ClassNotFoundException {
        List<String> listReference = new ArrayList<String>(statusFiles.keySet());
        return getMultipleFile(listReference,context);
    }

    public boolean removeSingleEvent(String docReference, Context context) throws IOException, ClassNotFoundException {
        removeSingleFile(docReference,context);
        Date elementRemoved = statusFiles.remove(docReference);
        boolean elementIsRemoved = (elementRemoved != null);
        return elementIsRemoved;
    }
}
