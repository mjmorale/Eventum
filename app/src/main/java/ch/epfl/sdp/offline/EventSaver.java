package ch.epfl.sdp.offline;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class EventSaver <Event> extends ObjectSaver {

    static private HashMap<String, Date> statusFiles = new HashMap<String, Date>();

    @NotNull
    @Override
    protected String getCollectionString() {
        return "Events";
    }

    /**
     * Saves an object in a file
     * @param deleteDate When we can delete the temp file
     * @param toSave The object to save in the collection
     * @param docReference Id of the document
     */
    public void saveFile(Event toSave, String docReference, Date deleteDate) throws IOException {
        super.saveFile((Serializable) toSave,docReference,deleteDate);

        //update status
        statusFiles.put(docReference, deleteDate);

        //remove old files
        for (String key: statusFiles.keySet()) {
            if (Objects.requireNonNull(statusFiles.get(key)).before(new Date())){
                //delete the file and remove from status
                File outDatedFile = File.createTempFile(docReference, null);
                outDatedFile.delete();
                statusFiles.remove(key);
            }
        }
    }

    public List<Event> getAllEvents() throws IOException, ClassNotFoundException {
        List<String> listReference = new ArrayList<String>(statusFiles.keySet());
        return getMultipleFile(listReference);
    }
}
