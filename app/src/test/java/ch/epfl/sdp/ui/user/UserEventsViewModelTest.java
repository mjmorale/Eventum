package ch.epfl.sdp.ui.user;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ch.epfl.sdp.db.Database;

public class UserEventsViewModelTest {

    @Mock
    private Database mDatabase;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void UserEventsViewModel_Constructor_SucceedsWithValidArgument() {
        UserEventsViewModel vm = new UserEventsViewModel(mDatabase);
    }
}
