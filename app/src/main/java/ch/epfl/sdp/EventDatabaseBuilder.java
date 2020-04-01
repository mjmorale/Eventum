package ch.epfl.sdp;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import ch.epfl.sdp.db.DatabaseObjectBuilder;

public class EventDatabaseBuilder extends DatabaseObjectBuilder<Event> {

    public EventDatabaseBuilder() {
        super(true, "title", "description", "date", "location");
    }

    @Override
    @NonNull
    public Event buildFromMap(@NonNull Map data) {
        super.buildFromMap(data);

        String title = (String) data.get("title");
        String description = (String) data.get("description");
        Timestamp timestamp = (Timestamp)data.get("date");
        GeoPoint location = (GeoPoint)data.get("location");
        return new Event(title, description, timestamp.toDate(), R.mipmap.ic_launcher, location);
    }

    @Override
    @NonNull
    public Map<String, Object> serializeToMap(@NonNull Event event) {
        super.serializeToMap(event);

        return new HashMap<String, Object>() {{
            put("title", event.getTitle());
            put("description", event.getDescription());
            put("date", new Timestamp(event.getDate()));
            put("location", event.getLocation());
        }};
    }

    @Nullable
    @Override
    public GeoPoint getLocation(@NonNull Event object) {
        super.getLocation(object);

        return object.getLocation();
    }
}
