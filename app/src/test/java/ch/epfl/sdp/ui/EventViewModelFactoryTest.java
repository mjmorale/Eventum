package ch.epfl.sdp.ui;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import ch.epfl.sdp.db.Database;

import static org.junit.Assert.assertEquals;

public class EventViewModelFactoryTest {

    private static final String DUMMY_STRING = "test";

    private static class InvalidConstructorViewModel extends EventViewModel {
        public InvalidConstructorViewModel(@NonNull Database database) {
            super(database, DUMMY_STRING);
        }
    }

    private static class PrivateConstructorViewModel extends EventViewModel {
        private PrivateConstructorViewModel(@NonNull Database database, @NonNull String eventRef) {
            super(database, eventRef);
        }
    }

    private static class ValidViewModel extends EventViewModel {
        public ValidViewModel(@NonNull Database database, @NonNull String eventRef) {
            super(database, eventRef);
        }
    }

    @Mock
    private Database mDatabase;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test (expected = IllegalArgumentException.class)
    public void EventViewModelFactory_Constructor_FailsWithNullFirstArgument() {
        EventViewModelFactory eventViewModelFactory = new EventViewModelFactory(null, DUMMY_STRING);
    }

    @Test (expected = IllegalArgumentException.class)
    public void EventViewModelFactory_Constructor_FailsWithNullSecondArgument() {
        EventViewModelFactory eventViewModelFactory = new EventViewModelFactory(mDatabase, null);
    }

    @Test
    public void EventViewModelFactory_Constructor_EventReferenceIsCorrectlyAssigned() {
        EventViewModelFactory eventViewModelFactory = new EventViewModelFactory(mDatabase, DUMMY_STRING);
        assertEquals(DUMMY_STRING, eventViewModelFactory.mEventRef);
    }

    @Test (expected = IllegalArgumentException.class)
    public void EventViewModelFactory_Create_ThrowsIfClassIsNotSubclassOfEventViewModel() {
        EventViewModelFactory eventViewModelFactory = new EventViewModelFactory(mDatabase, DUMMY_STRING);
        eventViewModelFactory.create(ViewModel.class);
    }

    @Test (expected = NullPointerException.class)
    public void EventViewModelFactory_Create_FailsWithNullArgument() {
        EventViewModelFactory eventViewModelFactory = new EventViewModelFactory(mDatabase, DUMMY_STRING);
        eventViewModelFactory.create(null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void EventViewModelFactory_Create_ThrowsIfNoConstructorDoesNotMatchArguments() {
        EventViewModelFactory eventViewModelFactory = new EventViewModelFactory(mDatabase, DUMMY_STRING);
        eventViewModelFactory.create(InvalidConstructorViewModel.class);
    }

    @Test (expected = IllegalArgumentException.class)
    public void EventViewModelFactory_Create_ThrowsIfConstructorIsPrivate() {
        EventViewModelFactory eventViewModelFactory = new EventViewModelFactory(mDatabase, DUMMY_STRING);
        eventViewModelFactory.create(PrivateConstructorViewModel.class);
    }

    @Test
    public void EventViewModelFactory_Create_CanInstantiateValidConstructorDefinition() {
        EventViewModelFactory eventViewModelFactory = new EventViewModelFactory(mDatabase, DUMMY_STRING);
        ValidViewModel vm = eventViewModelFactory.create(ValidViewModel.class);
        assertEquals(DUMMY_STRING, vm.mEventRef);
    }
}
