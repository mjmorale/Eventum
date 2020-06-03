package ch.epfl.sdp.ui.event;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import androidx.lifecycle.LiveData;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.auth.UserInfo;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.DocumentQuery;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EventViewModelTest {

    private static final String DUMMY_STRING = "test";
    private final static UserInfo DUMMY_USERINFO = new UserInfo("testuid", "testname", "testemail");

    @Mock
    private Database mDatabase;

    @Mock
    private Authenticator mAuthenticator;

    @Mock
    private CollectionQuery mCollectionQuery;

    @Mock
    private DocumentQuery mDocumentQuery;

    @Mock
    private LiveData<Event> mEventLiveData;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void DefaultEventViewModel_Constructor_ReferencesTheEventCollection() {
        when(mDatabase.query(anyString())).thenReturn(mCollectionQuery);
        when(mCollectionQuery.document(anyString())).thenReturn(mDocumentQuery);
        when(mAuthenticator.getCurrentUser()).thenReturn(DUMMY_USERINFO);
        doNothing().when(mDocumentQuery).getField(anyString(), any());
        EventViewModel vm = new EventViewModel(DUMMY_STRING, mAuthenticator, mDatabase);

        verify(mDatabase).query("events");
        verify(mCollectionQuery).document(DUMMY_STRING);
    }

    @Test
    public void DefaultEventViewModel_GetEvent_ReturnsANonNullValue() {
        when(mDatabase.query(anyString())).thenReturn(mCollectionQuery);
        when(mCollectionQuery.document(anyString())).thenReturn(mDocumentQuery);
        when(mDocumentQuery.liveData(Event.class)).thenReturn(mEventLiveData);
        when(mAuthenticator.getCurrentUser()).thenReturn(DUMMY_USERINFO);
        doNothing().when(mDocumentQuery).getField(anyString(), any());
        EventViewModel vm = new EventViewModel(DUMMY_STRING, mAuthenticator, mDatabase);

        assertNotNull(vm.getEvent());
    }
}
