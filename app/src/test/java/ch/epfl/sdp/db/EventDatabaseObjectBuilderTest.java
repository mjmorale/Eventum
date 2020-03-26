package ch.epfl.sdp.db;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import java.util.Date;
import java.util.Map;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.R;

import static org.junit.Assert.assertEquals;

public class EventDatabaseObjectBuilderTest {

    //TODO @Corentin: More tests

    @Test
    public void testEventDatabaseObjectBuilder() {
        Event event = new Event("OSS-117 Movie watching",
                "We will watch OSS-117: Cairo, Nest of Spies and then we can exchange about why this is the best movie of all times",
                new Date(2021, 1, 16), "Lausanne, Switzerland", new LatLng(46.520553, 6.567821), R.drawable.oss_117);

        Map<String, Object> data =
                DatabaseObjectBuilderRegistry.getBuilder(Event.class).serializeToMap(event);
        Event resultEvent = DatabaseObjectBuilderRegistry.getBuilder(Event.class).buildFromMap(data);

        assertEquals(event.getDate().toString(), resultEvent.getDate().toString());
        assertEquals(event.getDescription(), resultEvent.getDescription());
        assertEquals(event.getTitle(), resultEvent.getTitle());
        assertEquals(event.getAddress(), resultEvent.getAddress());
    }

}
