package ch.epfl.sdp.firebase.db.queries;
import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.db.DatabaseObjectBuilder;
import ch.epfl.sdp.db.DatabaseObjectBuilderFactory;
import ch.epfl.sdp.db.queries.FilterQuery;
import ch.epfl.sdp.firebase.db.FirestoreDatabase;
import ch.epfl.sdp.firebase.db.MockStringBuilder;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FirebaseDocumentLiveDataTest {

    private final static List<String> DUMMY_STRING = Collections.singletonList("test");

    @Mock
    private ListenerRegistration mListenerRegistration;

    @Mock
    private DocumentReference mDocumentReference;

    @Mock
    private DocumentSnapshot mDocumentSnapshot;

    @Captor
    private ArgumentCaptor<EventListener<DocumentSnapshot>> mDocumentSnapshotCompleteListenerCaptor;

    @Before
    public void setup() throws IllegalAccessException, InstantiationException {
        MockitoAnnotations.initMocks(this);
        DatabaseObjectBuilderFactory.clear();
        DatabaseObjectBuilderFactory.registerBuilder(String.class, MockStringBuilder.class);
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
        when(mDocumentSnapshot.getData()).thenReturn(null);

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
