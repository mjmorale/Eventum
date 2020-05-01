package ch.epfl.sdp.db;

/**
 * Represents a database object with an id and a value.
 * @param <T> The type of the object value
 */
public class DatabaseObject<T> {

    private T mObject;
    private String mId;

    /**
     * Construct a new DatabaseObject with an id an a value.
     *
     * @param id The id of the database object.
     * @param object The value of the database object.
     */
    public DatabaseObject(String id, T object) {
        mId = id;
        mObject = object;
    }

    /**
     * @return The id of the database object.
     */
    public String getId() {
        return mId;
    }

    /**
     * @return The value of the underlying object.
     */
    public T getObject() {
        return mObject;
    }
}
