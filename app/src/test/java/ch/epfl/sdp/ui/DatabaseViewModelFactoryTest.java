package ch.epfl.sdp.ui;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import ch.epfl.sdp.db.Database;

import static org.junit.Assert.assertEquals;

public class DatabaseViewModelFactoryTest {

    private static final String DUMMY_STRING = "test";

    private static class InvalidConstructorViewModel extends DatabaseViewModel {

        public InvalidConstructorViewModel(@NonNull Database database, String otherParam) {
            super(database);
        }
    }

    private class PrivateConstructorViewModel extends DatabaseViewModel {

        private PrivateConstructorViewModel(@NonNull Database database) {
            super(database);
        }
    }

    private static class ValidViewModel extends DatabaseViewModel {

        protected String mTest = DUMMY_STRING;

        public ValidViewModel(@NonNull Database database) {
            super(database);
        }
    }

    @Mock
    private ViewModel mViewModel;

    @Mock
    private Database mDatabase;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test (expected = IllegalArgumentException.class)
    public void DatabaseViewModelFactory_Constructor_FailsWithNullArgument() {
        DatabaseViewModelFactory factory = new DatabaseViewModelFactory(null);
    }

    @Test
    public void DatabaseViewModelFactory_Constructor_CorrectlyAssignsDatabase() {
        DatabaseViewModelFactory factory = new DatabaseViewModelFactory(mDatabase);
        assertEquals(mDatabase, factory.mDatabase);
    }

    @Test (expected = NullPointerException.class)
    public void DatabaseViewModelFactory_Create_ThrowsNullArgument() {
        DatabaseViewModelFactory factory = new DatabaseViewModelFactory(mDatabase);
        factory.create(null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void DatabaseViewModelFactory_Create_ThrowsWithClassThatIsNotASubclassOfDatabaseViewModel() {
        DatabaseViewModelFactory factory = new DatabaseViewModelFactory(mDatabase);
        factory.create(ViewModel.class);
    }

    @Test (expected = IllegalArgumentException.class)
    public void DatabaseViewModelFactory_Create_ThrowsWithClassThatDoesNotHaveAValidConstructor() {
        DatabaseViewModelFactory factory = new DatabaseViewModelFactory(mDatabase);
        factory.create(InvalidConstructorViewModel.class);
    }

    @Test (expected = IllegalArgumentException.class)
    public void DatabaseViewModelFactory_Create_ThrowsWithClassThatHasAnInaccessibleConstructor() {
        DatabaseViewModelFactory factory = new DatabaseViewModelFactory(mDatabase);
        factory.create(PrivateConstructorViewModel.class);
    }

    @Test
    public void DatabaseViewModelFactory_Create_WorksWithValidViewModelClass() {
        DatabaseViewModelFactory factory = new DatabaseViewModelFactory(mDatabase);
        ValidViewModel vm = factory.create(ValidViewModel.class);
        assertEquals(DUMMY_STRING, vm.mTest);
    }
}
