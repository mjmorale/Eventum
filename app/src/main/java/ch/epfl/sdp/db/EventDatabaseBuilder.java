package ch.epfl.sdp.db;

import com.google.firebase.Timestamp;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ch.epfl.sdp.Event;

public class EventDatabaseBuilder extends DatabaseObjectBuilder<Event> {

    public EventDatabaseBuilder() {
        super("title", "description", "date");
    }

    @Override
    public Event instantiateFromDatabase(Map<String, Object> data) throws IllegalArgumentException {
        checkRequiredFields(data);

        String title = (String)data.get("title");
        String description = (String)data.get("description");
        Date date = ((Timestamp)data.get("date")).toDate();
        return new Event(title, description, date);
    }

    @Override
    public Map<String, Object> serializeToDatabase(Event object) {
        Map<String, Object> data = new HashMap<>();
        data.put("title", object.getTitle());
        data.put("description", object.getDescription());
        data.put("date", new Timestamp(object.getDate()));

        return data;
    }
}
