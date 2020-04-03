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
}
