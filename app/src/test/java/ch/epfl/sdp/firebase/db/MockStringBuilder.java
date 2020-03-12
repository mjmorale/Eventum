package ch.epfl.sdp.firebase.db;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import ch.epfl.sdp.db.DatabaseObjectBuilder;

public class MockStringBuilder extends DatabaseObjectBuilder<String> {

    @Override
    public String buildFromMap(@NonNull Map<String, Object> data) {
        return (String) data.get("mock");
    }

    @Override
    public Map<String, Object> serializeToMap(@NonNull String object) {
        return new HashMap<String, Object>() {{
            put("mock", object);
        }};
    }
}