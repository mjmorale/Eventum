package ch.epfl.sdp.ui;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ch.epfl.sdp.db.Database;

import static org.junit.Assert.assertEquals;

public class DatabaseViewModelTest {

    @Mock
    private Database mDatabase;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test (expected = IllegalArgumentException.class)
    public void DatabaseViewModel_Constructor_FailsWithNullArgument() {
        DatabaseViewModel vm = new DatabaseViewModel(null);
    }

    @Test
    public void DatabaseViewModel_Constructor_AssignsDatabase() {
        DatabaseViewModel vm = new DatabaseViewModel(mDatabase);
        assertEquals(mDatabase, vm.mDatabase);
    }
}
