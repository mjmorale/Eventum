package ch.epfl.sdp.mocks;


import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.EventBuilder;
import ch.epfl.sdp.EventCategory;
import ch.epfl.sdp.R;

import static ch.epfl.sdp.EventCategory.Outdoor;
import static ch.epfl.sdp.EventCategory.Sport;

public class MockEvents {
    private static List<Event> eventList = populateEvents();

    /**
     * Initializes MockEvents
     */
    private static List<Event> populateEvents() {
        List<Event> eventList = new ArrayList<Event>();
        ArrayList<EventCategory> categories = new ArrayList<EventCategory>() {{
            add(Sport);
            add(Outdoor);
        }};

        eventList.add(new Event("OSS-117 Movie watching",
                "We will watch OSS-117.",
                new Date(2020, 1, 16), "Lausanne, Switzerland", new LatLng(46.520553, 6.567821), "URL", "user1", categories));
        eventList.add(new Event("Duck themed party",
                "Bring out your best duck disguises and join us for our amazing party on the lakeside. Swans disguises not allowed",
                new Date(2020, 3, 7), "Lausanne, Switzerland", new LatLng(46.520553, 6.567821), "URL", "user2", categories));
        eventList.add(new Event("Make Internet great again",
                "At this meeting we will debate on how to make pepe the frog memes great again",
                new Date(2020, 4, 20), "Lausanne, Switzerland", new LatLng(46.520553, 6.567821), "URL", "user3", categories));
        eventList.add(new Event("Real Fake Party",
                "This is really happening",
                new Date(2020, 11, 10), "Lausanne, Switzerland", new LatLng(46.520553, 6.567821), "URL", "user4", categories));

        return eventList;
    }

    /**
     *
     * @return The next Event to display
     */
    public static Event getNextEvent() {
        Event retVal = eventList.remove(0);
        eventList.add(retVal);
        return retVal;
    }

    public static Event getCurrentEvent() {
        return eventList.get(0);
    }
}
