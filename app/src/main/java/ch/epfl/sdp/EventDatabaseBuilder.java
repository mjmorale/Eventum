package ch.epfl.sdp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;
import java.util.Map;

import ch.epfl.sdp.db.DatabaseObjectBuilder;

public class EventDatabaseBuilder extends DatabaseObjectBuilder<Event> {
    @Override
    public Event buildFromMap(@NonNull Map data) {
        String title = (String) data.get("title");
        String description = (String) data.get("description");
        Timestamp timestamp = (Timestamp)data.get("date");
        GeoPoint location = (GeoPoint)data.get("location");
        String address = (String)data.get("address");
        String imageId = (String)data.get("imageId");
        String organizerRef = (String)data.get("organizer");

        EventBuilder eventBuilder = new EventBuilder();
        Event newEvent = eventBuilder.setTitle(title)
                .setDescription(description)
                .setDate(timestamp.toDate())
                .setAddress(address)
                .setLocation(new LatLng(location.getLatitude(), location.getLongitude()))
                .setImageId(imageId)
                .setOrganizerRef(organizerRef)
                .build();

        return newEvent;
    }

    @Override
    public boolean hasLocation() {
        return true;
    }

    @Override
    public Map<String, Object> serializeToMap(@NonNull Event event) {
        return new HashMap<String, Object>() {{
            put("title", event.getTitle());
            put("description", event.getDescription());
            put("date", new Timestamp(event.getDate()));
            put("address", event.getAddress());
            put("location", new GeoPoint(event.getLocation().latitude, event.getLocation().longitude));
            put("imageId", event.getImageId());
            put("organizer", event.getOrganizer());
        }};
    }

    @Nullable
    @Override
    public LatLng getLocation(@NonNull Event object) {
        return object.getLocation();
    }
}
