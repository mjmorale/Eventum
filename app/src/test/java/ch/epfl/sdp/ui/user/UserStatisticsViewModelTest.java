package ch.epfl.sdp.ui.user;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ch.epfl.sdp.db.Database;

public class UserStatisticsViewModelTest {

    @Mock
    private Database mDatabase;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void UserStatisticsViewModel_Constructor_SucceedsWithValidParameters() {
        UserStatisticsViewModel vm = new UserStatisticsViewModel(mDatabase);
    }
}
