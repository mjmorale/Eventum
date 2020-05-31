package ch.epfl.sdp;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.db.DatabaseObjectBuilderRegistry;

import static ch.epfl.sdp.EventCategory.Outdoor;
import static ch.epfl.sdp.EventCategory.Sport;
import static org.junit.Assert.assertEquals;

public class EventDatabaseObjectBuilderTest {

    //TODO @Corentin: More tests

    private ArrayList<EventCategory> categories = new ArrayList<EventCategory>() {{
        add(Sport);
        add(Outdoor);
    }};

    @Test
    public void EventDatabaseObjectBuilder_CheckSymmetry() {
        EventBuilder eventBuilder = new EventBuilder();
        Event event = eventBuilder.setTitle("Real Fake Party")
                .setDescription("This is really happening")
                .setDate(new Date(2020, 11, 10))
                .setImageId("URL")
                .setOrganizerRef("organizer")
                .setCategories(categories)
                .build();

        Map<String, Object> data =
                DatabaseObjectBuilderRegistry.getBuilder(Event.class).serializeToMap(event);
        Event resultEvent = DatabaseObjectBuilderRegistry.getBuilder(Event.class).buildFromMap(data);

        assertEquals(event.getDate().toString(), resultEvent.getDate().toString());
        assertEquals(event.getDescription(), resultEvent.getDescription());
        assertEquals(event.getTitle(), resultEvent.getTitle());
        assertEquals(event.getImageId(), resultEvent.getImageId());
        assertEquals(event.getOrganizer(), resultEvent.getOrganizer());
        assertEquals(event.getCategories(), resultEvent.getCategories());
    }

}
