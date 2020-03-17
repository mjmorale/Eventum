package ch.epfl.sdp.db;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

public abstract class DatabaseObjectBuilder<T> {

    private List<String> mRequiredFields;

    protected DatabaseObjectBuilder(String... requiredFields) {
        mRequiredFields = Arrays.asList(requiredFields);
    }

    public abstract T buildFromMap(@NonNull Map<String, Object> data);

    public abstract Map<String, Object> serializeToMap(@NonNull T object);

    protected void checkRequiredFields(Map<String, Object> data) {
        for(String field: mRequiredFields) {
            if(!data.containsKey(field)) {
                throw new IllegalArgumentException();
            }
        }
    }
}
