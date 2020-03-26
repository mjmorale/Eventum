package ch.epfl.sdp.ui;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ch.epfl.sdp.db.Database;

import static org.junit.Assert.assertEquals;

public class EventViewModelTest {

    private static final String DUMMY_STRING = "test";

    @Mock
    private Database mDatabase;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test (expected = IllegalArgumentException.class)
    public void EventViewModel_Constructor_FailsWithNullSecondArgument() {
        EventViewModel vm = new EventViewModel(mDatabase, null);
    }

    @Test
    public void EventViewModel_Constructor_CorrecltyAssignsEventReference() {
        EventViewModel vm = new EventViewModel(mDatabase, DUMMY_STRING);
        assertEquals(DUMMY_STRING, vm.mEventRef);
    }

}
