package ch.epfl.sdp.platforms.firebase.db.queries;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import ch.epfl.sdp.db.DatabaseObject;
import ch.epfl.sdp.db.DatabaseObjectBuilderRegistry;
import ch.epfl.sdp.platforms.firebase.db.queries.FirebaseQueryLiveData;
import ch.epfl.sdp.utils.MockStringBuilder;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FirebaseQueryLiveDataTest {

    private final static String DUMMY_STRING = "testString";
    private final static String DUMMY_ID = "testid";

    @Mock
    private Query mQuery;

    @Mock
    private ListenerRegistration mListenerRegistration;

    @Mock
    private QuerySnapshot mQuerySnapshot;

    @Mock
    private DocumentSnapshot mDocumentSnapshot;

    @Captor
    private ArgumentCaptor<EventListener<QuerySnapshot>> mEventListenerArgumentCaptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DatabaseObjectBuilderRegistry.registerBuilder(String.class, MockStringBuilder.class);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseQueryLiveData_Constructor_FailsWithNullFirstArgument() {
        FirebaseQueryLiveData<String> firebaseQueryLiveData = new FirebaseQueryLiveData<>(null, String.class);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseQueryLiveData_Constructor_FailsWithNullSecondArgument() {
        FirebaseQueryLiveData<String> firebaseQueryLiveData = new FirebaseQueryLiveData<>(mQuery, null);
    }

    @Test
    public void FirebaseQueryLiveData_Constructor_CreationDoesNotFail() {
        FirebaseQueryLiveData<String> firebaseQueryLiveData = new FirebaseQueryLiveData<>(mQuery, String.class);
    }

    @Test
    public void FirebaseQueryLiveData_OnActive_DoesNotFailWithNullParameters() {
        when(mQuery.addSnapshotListener(mEventListenerArgumentCaptor.capture())).thenReturn(mListenerRegistration);

        FirebaseQueryLiveData<String> firebaseQueryLiveData = new FirebaseQueryLiveData<>(mQuery, String.class);
        firebaseQueryLiveData.onActive();

        mEventListenerArgumentCaptor.getValue().onEvent(null, null);
    }

    @Test
    public void FirebaseQueryLiveData_OnActive_PostIsCalledWithDocumentValues() {
        when(mQuery.addSnapshotListener(mEventListenerArgumentCaptor.capture())).thenReturn(mListenerRegistration);
        when(mQuerySnapshot.getDocuments()).thenReturn(Arrays.asList(mDocumentSnapshot));
        when(mDocumentSnapshot.getData()).thenReturn(DatabaseObjectBuilderRegistry.getBuilder(String.class).serializeToMap(DUMMY_STRING));
        when(mDocumentSnapshot.getId()).thenReturn(DUMMY_ID);

        FirebaseQueryLiveData<String> firebaseQueryLiveData = new FirebaseQueryLiveData<>(mQuery, String.class);
        firebaseQueryLiveData.onActive();

        mEventListenerArgumentCaptor.getValue().onEvent(mQuerySnapshot, null);
    }

    @Test
    public void FirebaseQueryLiveData_OnInactive_RemovesListenerRegistrationIfSet() {
        when(mQuery.addSnapshotListener(any())).thenReturn(mListenerRegistration);

        FirebaseQueryLiveData<String> firebaseQueryLiveData = new FirebaseQueryLiveData<>(mQuery, String.class);
        firebaseQueryLiveData.onActive();

        firebaseQueryLiveData.onInactive();
        verify(mListenerRegistration).remove();
    }

    @Test
    public void FirebaseQueryLiveData_OnInactive_DoesNotFailWithNullListener() {
        FirebaseQueryLiveData<String> firebaseQueryLiveData = new FirebaseQueryLiveData<>(mQuery, String.class);
        firebaseQueryLiveData.onInactive();
    }
}
