package ch.epfl.sdp.ui.event;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import androidx.lifecycle.LiveData;
import ch.epfl.sdp.Event;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.DocumentQuery;
import ch.epfl.sdp.map.MapManager;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DefaultEventViewModelTest {

    private static final String DUMMY_STRING = "test";

    @Mock
    private Database mDatabase;

    @Mock
    private CollectionQuery mCollectionQuery;

    @Mock
    private DocumentQuery mDocumentQuery;

    @Mock
    private LiveData<Event> mEventLiveData;

    @Mock
    private MapManager mMapManagerMock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void DefaultEventViewModel_Constructor_ReferencesTheEventCollection() {
        when(mDatabase.query(anyString())).thenReturn(mCollectionQuery);
        when(mCollectionQuery.document(anyString())).thenReturn(mDocumentQuery);
        DefaultEventViewModel vm = new DefaultEventViewModel(DUMMY_STRING, mDatabase);

        verify(mDatabase).query("events");
        verify(mCollectionQuery).document(DUMMY_STRING);
    }

    @Test
    public void DefaultEventViewModel_GetEvent_ReturnsANonNullValue() {
        when(mDatabase.query(anyString())).thenReturn(mCollectionQuery);
        when(mCollectionQuery.document(anyString())).thenReturn(mDocumentQuery);
        when(mDocumentQuery.livedata(Event.class)).thenReturn(mEventLiveData);
        DefaultEventViewModel vm = new DefaultEventViewModel(DUMMY_STRING, mDatabase);

        assertNotNull(vm.getEvent());
    }
}
