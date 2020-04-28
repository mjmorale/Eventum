package ch.epfl.sdp.offline;

import java.util.List;

public class EventSaver <Event> extends ObjectSaver {

    @Override
    protected String getCollectionString() {
        return "Events";
    }

    public List<Event> getAllEvents(){

        return null;
    }
}
