package ch.epfl.sdp;

import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Date;

import static org.junit.Assert.*;


import ch.epfl.sdp.Event;

public class EventTest {

    String title = "Real Fake Event";
    String description = "This is really happening";
    Date date = new Date(2020,11,10);

    @Test
    public void testDummyEvent()
    {
        Event e = new Event(title, description, date);
        assertEquals(e.getTitle(), title);
        assertEquals(e.getDescription(), description);
        assertEquals(e.getDate(), date);
    }

    // An attribute of an Event should not be null
    @Test(expected = IllegalArgumentException.class)
    public void testTitleNotNull()
    {
        Event e = new Event(null, description, date);
    }

    // An attribute of an Event should not be null
    @Test(expected = IllegalArgumentException.class)
    public void testDescriptionNotNull()
    {
        Event e = new Event(title, null, date);
    }

    // An attribute of an Event should not be null
    @Test(expected = IllegalArgumentException.class)
    public void testDateNotNull()
    {
        Event e = new Event(title, description, null);
    }

    // An attribute of an Event should not be null
    @Test(expected = IllegalArgumentException.class)
    public void testSetTitleFailsOnNull()
    {
        Event e = new Event(title,description,date);
        e.setTitle(null);
    }

    // An attribute of an Event should not be null
    @Test(expected = IllegalArgumentException.class)
    public void testSetDescFailsOnNull()
    {
        Event e = new Event(title,description,date);
        e.setDescription(null);
    }

    // An attribute of an Event should not be null
    @Test(expected = IllegalArgumentException.class)
    public void testSetDateFailsOnNull()
    {
        Event e = new Event(title,description,date);
        e.setDate(null);
    }
}
