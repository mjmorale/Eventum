package ch.epfl.sdp.ui.main;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.DocumentQuery;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MainViewModelTest {

    private static final String DUMMY_USERREF = "sdfkljghp23ouih4dsfg";

    @Mock
    private Database mDatabase;

    @Mock
    private CollectionQuery mCollectionQuery;

    @Mock
    private DocumentQuery mDocumentQuery;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void MainViewModel_Constructor_PerformCorrectUserQuery() {
        when(mDatabase.query(anyString())).thenReturn(mCollectionQuery);
        when(mCollectionQuery.document(anyString())).thenReturn(mDocumentQuery);

        MainViewModel vm = new MainViewModel(DUMMY_USERREF, mDatabase);

        verify(mDatabase).query("users");
        verify(mCollectionQuery).document(DUMMY_USERREF);
    }

    @Test
    public void MainViewModel_GetUserRef_ReturnsCorrectValue() {
        when(mDatabase.query(anyString())).thenReturn(mCollectionQuery);
        when(mCollectionQuery.document(anyString())).thenReturn(mDocumentQuery);

        MainViewModel vm = new MainViewModel(DUMMY_USERREF, mDatabase);

        assertEquals(DUMMY_USERREF, vm.getUserRef());
    }
}
