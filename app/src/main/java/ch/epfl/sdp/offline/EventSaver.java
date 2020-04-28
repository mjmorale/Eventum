package ch.epfl.sdp.offline;

public class EventSaver <Event> extends ObjectSaver {

    @Override
    protected String getCollectionString() {
        return "Events";
    }
}
