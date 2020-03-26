package ch.epfl.sdp;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Date;

import static org.junit.Assert.*;


import ch.epfl.sdp.Event;

public class EventTest {

    String title = "Real Fake Event";
    String description = "This is really happening";
    Date date = new Date(2020,11,10);
    LatLng location = new LatLng(100,100);
    String address = "Lausanne, Switzerland";

    @Test
    public void testDummyEvent()
    {
        Event e = new Event(title, description, date, address, location);
        assertEquals(e.getTitle(), title);
        assertEquals(e.getDescription(), description);
        assertEquals(e.getDate(), date);
        assertEquals(e.getAddress(), address);
    }

    @Test
    public void testSetTitle()
    {
        Event e = new Event(title, description, date, address, location);
        e.setTitle("New title");
    }

    @Test
    public void testSetDescription()
    {
        Event e = new Event(title, description, date, address, location);
        e.setDescription("New description");
    }

    @Test
    public void testSetLocation()
    {
        Event e = new Event(title,description, date, address, location);
        e.setLocation(location);
    }

    @Test
    public void testSetDate()
    {
        Event e = new Event(title, description, date, address, location);
        e.setDate(new Date());
    }

    @Test
    public void testSetImageId(){
        Event e = new Event(title, description, date, address, location);
        e.setImageID(R.drawable.oss_117);
        assertEquals(e.getImageID(), R.drawable.oss_117);
    }

    @Test
    public void testSetAddress(){
        Event e = new Event(title, description, date, address, location);
        e.setAddress(address);
        assertEquals(e.getAddress(), address);
    }
}
