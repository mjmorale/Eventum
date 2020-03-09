package ch.epfl.sdp;

import android.util.Log;

import com.google.firebase.Timestamp;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import ch.epfl.sdp.db.DatabaseObjectBuilder;

public class DatabaseEventBuilder extends DatabaseObjectBuilder<Event> {

    public DatabaseEventBuilder() {
        super("title", "description", "date");
    }

    @Override
    public Event buildFromMap(@NonNull Map<String, Object> data) {
        if(data == null) {
            throw new IllegalArgumentException();
        }
        checkRequiredFields(data);

        String title = (String) data.get("title");
        String description = (String) data.get("description");
        Date date = ((Timestamp) data.get("date")).toDate();
        return new Event(title, description, date);
    }

    @Override
    public Map<String, Object> serializeToMap(@NonNull Event object) {
        if(object == null) {
            throw new IllegalArgumentException();
        }
        Map<String, Object> data = new HashMap<>();

        data.put("title", object.getTitle());
        data.put("description", object.getDescription());
        data.put("date", new Timestamp(object.getDate()));
        return data;
    }
}
