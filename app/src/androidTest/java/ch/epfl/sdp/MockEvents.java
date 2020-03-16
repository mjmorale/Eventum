package ch.epfl.sdp;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MockEvents {
    private static List<Event> eventList = populateEvents();

    /**
     * Initializes MockEvents
     */
    private static List<Event> populateEvents() {
        List<Event> eventList = new ArrayList<Event>();

        eventList.add(new Event("Real Fake Party",
                "This is really happening",
                new Date(2020, 11, 10)));
        eventList.add(new Event("Duck themed party",
                "Bring out your best duck disguises and join us for our amazing party on the lakeside. Swans disguises not allowed",
                new Date(2020, 3, 7)));
        eventList.add(new Event("Make Internet great again",
                "At this meeting we will debate on how to make pepe the frog memes great again",
                new Date(2020, 4, 20)));
        eventList.add(new Event("OSS-117 Movie watching",
                "We will watch OSS-117: Cairo, Nest of Spies and then we can exchange about why this is the best movie of all times",
                new Date(2021, 1, 16)));

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
