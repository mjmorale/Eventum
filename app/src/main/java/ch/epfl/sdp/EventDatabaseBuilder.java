package ch.epfl.sdp;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

import java.util.HashMap;
import java.util.Map;

import ch.epfl.sdp.db.DatabaseObjectBuilder;

public class EventDatabaseBuilder extends DatabaseObjectBuilder<Event> {
    @Override
    public Event buildFromMap(@NonNull Map data) {
        String title = (String) data.get("title");
        String description = (String) data.get("description");
        Timestamp timestamp = (Timestamp)data.get("date");
        return new Event(title, description, timestamp.toDate());
    }

    @Override
    public Map<String, Object> serializeToMap(@NonNull Event event) {
        return new HashMap<String, Object>() {{
            put("title", event.getTitle());
            put("description", event.getDescription());
            put("date", new Timestamp(event.getDate()));
        }};
    }
}
