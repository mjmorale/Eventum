package ch.epfl.sdp;

import org.junit.Test;

import java.util.Date;
import java.util.Map;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.db.DatabaseObjectBuilderRegistry;

import static org.junit.Assert.assertEquals;

public class EventDatabaseObjectBuilderTest {

    //TODO @Corentin: More tests

    @Test
    public void EventDatabaseObjectBuilder_CheckSymmetry() {
        EventBuilder eventBuilder = new EventBuilder();
        Event event = eventBuilder.setTitle("Real Fake Party")
                .setDescription("This is really happening")
                .setDate(new Date(2020, 11, 10))
                .build();

        Map<String, Object> data =
                DatabaseObjectBuilderRegistry.getBuilder(Event.class).serializeToMap(event);
        Event resultEvent = DatabaseObjectBuilderRegistry.getBuilder(Event.class).buildFromMap(data);

        assertEquals(event.getDate().toString(), resultEvent.getDate().toString());
        assertEquals(event.getDescription(), resultEvent.getDescription());
        assertEquals(event.getTitle(), resultEvent.getTitle());
    }

}
