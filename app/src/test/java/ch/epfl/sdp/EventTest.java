package ch.epfl.sdp;

import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.content.Context;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Date;
import java.util.List;

import ch.epfl.sdp.offline.EventSaver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class EventTest  {

    private String title = "title";
    private String description = "This is really happening";
    private Date date = new Date(2020,11,10);
    private LatLng location = new LatLng(100,100);
    private String address = "Lausanne, Switzerland";
    private Context context = new Activity();
    private Date dateEvent = new Date();


    String TEST_FILE = "EventTest.txt";

    @Test
    public void EventBuilder_CheckCorrectData()
    {
        EventBuilder eventBuilder = new EventBuilder();
        Event e = eventBuilder.setTitle(title)
                              .setDescription(description)
                              .setDate(date)
                              .setAddress(address)
                              .setLocation(location)
                              .build();

        assertEquals(e.getTitle(), title);
        assertEquals(e.getDescription(), description);
        assertEquals(e.getDate(), date);
        assertEquals(e.getAddress(), address);
    }

    @Test
    public void Event_AddingEvent() {
        Event event = new Event(title+"testAddEvent", description, date, address, location, 0);
        try{
            EventSaver eventSaver = new EventSaver();
            dateEvent.setYear(dateEvent.getYear()+1);
            eventSaver.saveEvent(event,"testAddEvent",dateEvent,context);

            File newFile = new File(context.getFilesDir(),"testAddEvent");

            FileInputStream fi = new FileInputStream(newFile);
            ObjectInputStream oi = new ObjectInputStream(fi);

            Event eventRead = (Event) oi.readObject();

            oi.close();
            fi.close();

            assertEquals(event, eventRead);

            //clean files
            eventSaver.removeSingleEvent("testAddEvent",context);
        } catch (Exception exc){
            exc.printStackTrace();
        }

    }

    @Test
    public void Event_getAllEvents() {
        Event event = new Event(title+"testGetAllEvent1", description, date, address, location, 0);
        Event event2 = new Event(title+"testGetAllEvent2", description, date, address, location, 0);
        try{
            EventSaver eventSaver = new EventSaver();
            dateEvent.setYear(dateEvent.getYear()+1);
            eventSaver.saveEvent(event,"testGetAllEvent1",dateEvent,context);
            eventSaver.saveEvent(event2,"testGetAllEvent2",dateEvent,context);

            List listEvent = eventSaver.getAllEvents(context);
            assertTrue(listEvent.contains(event));
            assertTrue(listEvent.contains(event2));

            //clean files
            eventSaver.removeSingleEvent("testGetAllEvent1",context);
            eventSaver.removeSingleEvent("testGetAllEvent2",context);

        } catch (Exception exc){
            exc.printStackTrace();
        }
    }

    @Test
    public void Event_AddingEventAndRemovedBecauseOutdated() {
        Event event = new Event(title+"testOutdatedEvent", description, date, address, location, 0);
        try{
            EventSaver eventSaver = new EventSaver();
            dateEvent.setYear(dateEvent.getYear()-1);
            eventSaver.saveEvent(event,"testOutdatedEvent",dateEvent,context);

            List listEvent = eventSaver.getAllEvents(context);
            assertFalse(listEvent.contains(event));
        } catch (Exception exc){
            exc.printStackTrace();
        }
    }

    @Test
    public void Event_getSingleFile() {
        Event event = new Event(title+"testGetSingleFile", description, date, address, location, 0);
        try{
            EventSaver eventSaver = new EventSaver();
            dateEvent.setYear(dateEvent.getYear()+1);
            eventSaver.saveEvent(event,"testGetSingleFile",dateEvent,context);

            Event eventResult = (Event)eventSaver.getSingleFile("testGetSingleFile", context);
            assertNotNull(eventResult);
            assertEquals(eventResult,event);
            //clean files
            eventSaver.removeSingleEvent("testGetSingleFile",context);
        } catch (Exception exc){
            exc.printStackTrace();
        }
    }

    @Test
    public void Event_removeFileNotExist() throws IOException, ClassNotFoundException {
        EventSaver eventSaver = new EventSaver();
        boolean beenRemoved = eventSaver.removeSingleEvent("testRemoveWrong",context);
        assertFalse(beenRemoved);
    }

    @Test
    public void Event_removeFileExist() throws IOException, ClassNotFoundException {
        Event event = new Event(title, description, date, address, location, 0);
        EventSaver eventSaver = new EventSaver();
        dateEvent.setYear(dateEvent.getYear()+1);

        eventSaver.saveEvent(event,"testRemoveOK",dateEvent,context);

        boolean beenRemoved = eventSaver.removeSingleEvent("testRemoveOK",context);
        assertTrue(beenRemoved);
    }
}
