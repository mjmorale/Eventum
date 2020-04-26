package ch.epfl.sdp.Offline;

public class EventSaver <Event> extends ObjectSaver {

    @Override
    protected String getCollectionString() {
        return "Events";
    }
}
