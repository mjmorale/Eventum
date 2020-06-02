package ch.epfl.sdp.ui.event;

import org.junit.Test;

import java.util.ArrayList;

public class AttendeeListAdapterTest {

    @Test(expected = IllegalArgumentException.class)
    public void AttendeeListAdapter_FirstConstructor_FailsWithNullFirstArgument() {
        AttendeeListAdapter adapter = new AttendeeListAdapter(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void AttendeeListAdapter_SecondConstructor_FailsWithNullFirstArgument() {
        AttendeeListAdapter adapter = new AttendeeListAdapter(null, new ArrayList<>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void AttendeeListAdapter_SecondConstructor_FailsWithNullSecondArgument() {
        AttendeeListAdapter adapter = new AttendeeListAdapter("id", null);
    }
}
