package ch.epfl.sdp.firebase.db.queries;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.ListenerRegistration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import ch.epfl.sdp.db.DatabaseObjectBuilderRegistry;
import ch.epfl.sdp.utils.MockStringBuilder;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FirebaseDocumentLiveDataTest {

    @Mock
    private ListenerRegistration mListenerRegistration;

    @Mock
    private DocumentReference mDocumentReference;

    @Mock
    private DocumentSnapshot mDocumentSnapshot;

    @Captor
    private ArgumentCaptor<EventListener<DocumentSnapshot>> mDocumentSnapshotCompleteListenerCaptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DatabaseObjectBuilderRegistry.registerBuilder(String.class, MockStringBuilder.class);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseDocumentLiveData_Constructor_FailsWithNullFirstArgument() {
        FirebaseDocumentLiveData<String> stringLiveData = new FirebaseDocumentLiveData<>(null, String.class);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseDocumentLiveData_Constructor_FailsWithNullSecondArgument() {
        FirebaseDocumentLiveData<String> stringLiveData = new FirebaseDocumentLiveData<>(mDocumentReference, null);
    }

    @Test
    public void FirebaseDocumentLiveData_Constructor_CreationDoesNotFail() {
        FirebaseDocumentLiveData<String> stringLiveData = new FirebaseDocumentLiveData<>(mDocumentReference, String.class);
    }

    @Test
    public void FirebaseDocumentLiveData_OnActive_DoesNotFailWithNullDataSnapshot() {
        when(mDocumentReference.addSnapshotListener(mDocumentSnapshotCompleteListenerCaptor.capture())).thenReturn(mListenerRegistration);
        when(mDocumentSnapshot.getData()).thenReturn(null);

        FirebaseDocumentLiveData<String> stringLiveData = new FirebaseDocumentLiveData<>(mDocumentReference, String.class);
        stringLiveData.onActive();

        mDocumentSnapshotCompleteListenerCaptor.getValue().onEvent(mDocumentSnapshot,null);
    }

    @Test
    public void FirebaseDocumentLiveData_OnInactive_RemovesListenerRegistrationIfSet() {
        when(mDocumentReference.addSnapshotListener(any())).thenReturn(mListenerRegistration);

        FirebaseDocumentLiveData<String> stringLiveData = new FirebaseDocumentLiveData<>(mDocumentReference, String.class);
        stringLiveData.onActive();

        stringLiveData.onInactive();
        verify(mListenerRegistration).remove();
    }

    @Test
    public void FirebaseDocumentLiveData_OnInactive_DoesNotFailIfNoListenerRegistration() {
        FirebaseDocumentLiveData<String> stringLiveData = new FirebaseDocumentLiveData<>(mDocumentReference, String.class);

        stringLiveData.onInactive();
    }
}
