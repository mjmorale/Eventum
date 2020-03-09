package ch.epfl.sdp.db;

import java.util.Map;

public abstract class DatabaseObjectBuilder<T> {

    public abstract T buildFromMap(Map<String, Object> data);

    public abstract Map<String, Object> serializeToMap(T object);
}
