package ch.epfl.sdp.db;

import java.util.Map;

public abstract class DatabaseObjectBuilder<T> {

    String[] mRequiredFields;

    public DatabaseObjectBuilder(String... requiredFields) {
        this.mRequiredFields = requiredFields;
    }

    protected void checkRequiredFields(Map<String, Object> data) throws IllegalArgumentException {
        for(String field: mRequiredFields) {
            if(!data.containsKey(field)) {
                throw new IllegalArgumentException();
            }
        }
    }

    public abstract T instantiateFromDatabase(Map<String, Object> data) throws IllegalArgumentException;

    public abstract Map<String, Object> serializeToDatabase(T object);
}
