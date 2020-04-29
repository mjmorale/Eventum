package ch.epfl.sdp.ui.event;


import com.google.android.gms.maps.model.LatLng;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.EventBuilder;
import ch.epfl.sdp.R;
import ch.epfl.sdp.db.Database;

// This is an example implementation of an instrumented fragment test.
@RunWith(MockitoJUnitRunner.class)
public class DefaultEventFragmentTest {

    @Mock
    private Database mDatabaseMock;

    // Date representing September 27 2020 in milliseconds
    private Date date = new Date(1601234567890L);

    private EventBuilder eventBuilder = new EventBuilder();
    private Event eventTest = eventBuilder.setTitle("title").setDescription("description").setDate("01/01/2021").build();

}
