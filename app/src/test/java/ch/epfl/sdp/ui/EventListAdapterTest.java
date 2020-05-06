package ch.epfl.sdp.ui;

import org.junit.Test;

public class EventListAdapterTest {

    @Test (expected = IllegalArgumentException.class)
    public void EventListAdapter_Constructor_FailsWithNullParameter() {
        EventListAdapter adapter = new EventListAdapter(null);
    }

}
