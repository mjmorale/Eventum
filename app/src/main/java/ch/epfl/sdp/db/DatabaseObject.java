package ch.epfl.sdp.db;

public class DatabaseObject<T> {

    private T mObject;
    private String mId;

    public DatabaseObject(String id, T object) {
        mId = id;
        mObject = object;
    }

    public String getId() {
        return mId;
    }

    public T getObject() {
        return mObject;
    }
}
