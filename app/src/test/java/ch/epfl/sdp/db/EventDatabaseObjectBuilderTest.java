package ch.epfl.sdp.db;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.Map;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.EventDatabaseBuilder;

import static org.junit.Assert.assertEquals;

public class EventDatabaseObjectBuilderTest {

    @Before
    public void setup() throws IllegalAccessException, InstantiationException {
        DatabaseObjectBuilderFactory.registerBuilder(Event.class, EventDatabaseBuilder.class);
    }

    @Test
    public void testEventDatabaseObjectBuilder() {
        Event event = new Event("Real Fake Party",
                "This is really happening",
                new Date(2020, 11, 10));
        Map<String, Object> data =
                DatabaseObjectBuilderFactory.getBuilder(Event.class).serializeToMap(event);
        Event resultEvent = DatabaseObjectBuilderFactory.getBuilder(Event.class).buildFromMap(data);

        assertEquals(event.getDate().toString(), resultEvent.getDate().toString());
        assertEquals(event.getDescription(), resultEvent.getDescription());
        assertEquals(event.getTitle(), resultEvent.getTitle());
    }

}
