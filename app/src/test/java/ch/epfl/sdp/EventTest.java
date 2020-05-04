package ch.epfl.sdp;

import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.content.Context;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;

import ch.epfl.sdp.offline.EventSaver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class EventTest  {

    private String title = "title";
    private String description = "This is really happening";
    private Date date = new Date(2020,11,10);
    private LatLng location = new LatLng(100,100);
    private String address = "Lausanne, Switzerland";
    private Context context = new Activity();
    private File path = context.getFilesDir();
    private Date dateEvent = new Date();
    String organizerRef = "organizerref";
    String imageId = "URL";



    @Test
    public void EventBuilder_CheckCorrectData()
    {
        EventBuilder eventBuilder = new EventBuilder();
        Event e = eventBuilder.setTitle(title)
                              .setDescription(description)
                              .setDate(date)
                              .setAddress(address)
                              .setLocation(location)
                              .setImageId(imageId)
                              .setOrganizerRef(organizerRef)
                              .build();

        assertEquals(e.getTitle(), title);
        assertEquals(e.getDescription(), description);
        assertEquals(e.getDate(), date);
        assertEquals(e.getAddress(), address);
        assertEquals(e.getImageId(), imageId);
        assertEquals(e.getOrganizer(), organizerRef);
    }

    @Test
    public void Event_AddingEvent() throws IOException, ClassNotFoundException {
        Event event = new Event(title+"testAddEvent", description, date, address, location, imageId, organizerRef);
        EventSaver eventSaver = new EventSaver();
        dateEvent.setYear(dateEvent.getYear()+1);
        eventSaver.saveEvent(event,"testAddEvent",dateEvent,path);

        File newFile = new File(context.getFilesDir(),"testAddEvent");

        FileInputStream fi = new FileInputStream(newFile);
        ObjectInputStream oi = new ObjectInputStream(fi);

        Event eventRead = (Event) oi.readObject();

        oi.close();
        fi.close();

        assertEquals(event, eventRead);

        //clean files
        eventSaver.removeSingleEvent("testAddEvent",path);
        eventSaver.removeSingleFile("eventStatusFiles",path);
    }

    @Test
    public void Event_AddingSameEvent() throws IOException, ClassNotFoundException {
        Event event = new Event(title+"testAddEvent", description, date, address, location, imageId, organizerRef);
        EventSaver eventSaver = new EventSaver();
        dateEvent.setYear(dateEvent.getYear()+1);
        eventSaver.saveEvent(event,"testAddEvent",dateEvent,path);
        eventSaver.saveEvent(event,"testAddEvent",dateEvent,path);

        List listEvent = eventSaver.getAllEvents(path);
        assertEquals(listEvent.size(),1);

        //clean files
        eventSaver.removeSingleEvent("testAddEvent",path);
        eventSaver.removeSingleFile("eventStatusFiles",path);
    }

    @Test
    public void Event_getAllEvents() throws IOException, ClassNotFoundException {
        Event event = new Event(title+"testGetAllEvent1", description, date, address, location, imageId, organizerRef);
        Event event2 = new Event(title+"testGetAllEvent2", description, date, address, location, imageId, organizerRef);

        EventSaver eventSaver = new EventSaver();
        dateEvent.setYear(dateEvent.getYear()+1);
        eventSaver.saveEvent(event,"testGetAllEvent1",dateEvent,path);
        eventSaver.saveEvent(event2,"testGetAllEvent2",dateEvent,path);

        List listEvent = eventSaver.getAllEvents(path);
        assertTrue(listEvent.contains(event));
        assertTrue(listEvent.contains(event2));

        //clean files
        eventSaver.removeSingleEvent("testGetAllEvent1",path);
        eventSaver.removeSingleEvent("testGetAllEvent2",path);
        eventSaver.removeSingleFile("eventStatusFiles",path);
    }

    @Test
    public void Event_AddingEventAndRemovedBecauseOutdated() throws IOException, ClassNotFoundException {
        Event event = new Event(title+"testOutdatedEvent", description, date, address, location, imageId, organizerRef);

        EventSaver eventSaver = new EventSaver();
        dateEvent.setYear(dateEvent.getYear()-1);
        eventSaver.saveEvent(event,"testOutdatedEvent",dateEvent,path);

        List listEvent = eventSaver.getAllEvents(path);
        assertFalse(listEvent.contains(event));

        //clean files
        eventSaver.removeSingleFile("eventStatusFiles",path);
    }

    @Test
    public void Event_getSingleFile() throws IOException, ClassNotFoundException {
        Event event = new Event(title+"testGetSingleFile", description, date, address, location, imageId, organizerRef);
        EventSaver eventSaver = new EventSaver();
        dateEvent.setYear(dateEvent.getYear()+1);
        eventSaver.saveEvent(event,"testGetSingleFile",dateEvent,path);

        Event eventResult = (Event)eventSaver.getSingleFile("testGetSingleFile", path);
        assertNotNull(eventResult);
        assertEquals(eventResult,event);

        //clean files
        eventSaver.removeSingleEvent("testGetSingleFile",path);
        eventSaver.removeSingleFile("eventStatusFiles",path);
    }

    @Test
    public void Event_removeFileNotExist() throws IOException, ClassNotFoundException {
        EventSaver eventSaver = new EventSaver();
        boolean beenRemoved = eventSaver.removeSingleEvent("testRemoveWrong",path);
        assertFalse(beenRemoved);
        //clean files
        eventSaver.removeSingleFile("eventStatusFiles",path);
    }

    @Test
    public void Event_removeFileExist() throws IOException, ClassNotFoundException {
        Event event = new Event(title, description, date, address, location, imageId, organizerRef);
        EventSaver eventSaver = new EventSaver();
        dateEvent.setYear(dateEvent.getYear()+1);

        eventSaver.saveEvent(event,"testRemoveOK",dateEvent,path);

        boolean beenRemoved = eventSaver.removeSingleEvent("testRemoveOK",path);
        assertTrue(beenRemoved);
        //clean files
        eventSaver.removeSingleFile("eventStatusFiles",path);
    }
}
