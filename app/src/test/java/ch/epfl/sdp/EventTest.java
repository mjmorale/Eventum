package ch.epfl.sdp;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.spec.ECField;
import java.util.Date;

import static org.junit.Assert.*;


import ch.epfl.sdp.Event;

public class EventTest {

    String title = "Real Fake Event";
    String description = "This is really happening";
    Date date = new Date(2020,11,10);
    LatLng location = new LatLng(100,100);
    String address = "Lausanne, Switzerland";

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
    public void Event_WrittenAndReadCorrectlyFromFile(){
        Event e = new Event(title, description, date, address, location, 0);
        try{
            FileOutputStream f = new FileOutputStream(new File(TEST_FILE));
            ObjectOutputStream o = new ObjectOutputStream((f));

            o.writeObject(e);

            o.close();
            f.close();

            FileInputStream fi = new FileInputStream(new File(TEST_FILE));
            ObjectInputStream oi = new ObjectInputStream(fi);

            Event eventRead = (Event) oi.readObject();

            oi.close();
            fi.close();

            assertEquals(e, eventRead);

        } catch (Exception exc){
            exc.printStackTrace();
        }
    }
}
