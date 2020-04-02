package ch.epfl.sdp.utils;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import ch.epfl.sdp.db.DatabaseObjectBuilder;

public class MockStringBuilder extends DatabaseObjectBuilder<String> {

    public MockStringBuilder() {
        super(false,"mock");
    }

    @Override
    public String buildFromMap(@NonNull Map<String, Object> data) {
        checkRequiredFields(data);
        return (String) data.get("mock");
    }

    @Override
    public Map<String, Object> serializeToMap(@NonNull String object) {
        Map<String, Object> result = new HashMap<>();
        result.put("mock", object);
        return result;
    }
}