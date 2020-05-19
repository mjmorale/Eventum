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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.epfl.sdp.offline.EventSaver;

import static ch.epfl.sdp.EventCategory.Outdoor;
import static ch.epfl.sdp.EventCategory.Sport;
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
    private String organizerRef = "organizerref";
    private String imageId = "URL";
    private ArrayList<EventCategory> categories = new ArrayList<EventCategory>() {{
        add(Sport);
        add(Outdoor);
    }};

    @Test
    public void EventBuilder_CheckCorrectData() {
        EventBuilder eventBuilder = new EventBuilder();
        Event e = eventBuilder.setTitle(title)
                              .setDescription(description)
                              .setDate(date)
                              .setAddress(address)
                              .setLocation(location)
                              .setImageId(imageId)
                              .setOrganizerRef(organizerRef)
                              .setCategories(categories)
                              .build();

        assertEquals(e.getTitle(), title);
        assertEquals(e.getDescription(), description);
        assertEquals(e.getDate(), date);
        assertEquals(e.getAddress(), address);
        assertEquals(e.getImageId(), imageId);
        assertEquals(e.getOrganizer(), organizerRef);
        assertEquals(e.getCategories(), categories);
    }
}
