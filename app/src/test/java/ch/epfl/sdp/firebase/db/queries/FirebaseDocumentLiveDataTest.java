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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FirebaseDocumentLiveDataTest {
    @Mock
    private FirebaseFirestore mDb;

    @Mock
    private Query mQuery;

    @Mock
    private ListenerRegistration mListenerRegistration;

    @Mock
    private DocumentReference mDocumentReference;

    @Mock
    private QuerySnapshot mQuerySnapshot;

    @Mock
    private Map<String, Object> mMapStringObj;

    @Mock
    private DocumentSnapshot mDocumentSnapshot;

    private final static List<String> DUMMY_STRING = Collections.singletonList("test");

    @Captor
    private ArgumentCaptor<EventListener<DocumentSnapshot>> mDocumentSnapshotCompleteListenerCaptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DatabaseObjectBuilderFactory.clear();
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseDocumentLiveDataTest_Constructor_FailsWithNullArgument() {
        FirebaseDocumentLiveData FDLD = new FirebaseDocumentLiveData(null, String.class);
    }

    @Test
    public void FirebaseDocumentLiveDataTest_Constructor() {
        FirebaseDocumentLiveData FDLD = new FirebaseDocumentLiveData(mDocumentReference,String.class);
    }

    @Test
    public void FirebaseDocumentLiveDataTest_Active_nullDocumentSnapshot() {
        FirebaseDocumentLiveData FDLD = new FirebaseDocumentLiveData(mDocumentReference,String.class);

        when(mDocumentReference.addSnapshotListener(mDocumentSnapshotCompleteListenerCaptor.capture())).thenReturn(null);
        Map<String, Object> mMapStringObj2 = new HashMap<>();
        mMapStringObj2.put("England", "London");
        when(mDocumentSnapshot.getData()).thenReturn( null);
        FDLD.onActive();
        mDocumentSnapshotCompleteListenerCaptor.getValue().onEvent(mDocumentSnapshot,null);
    }

    @Test
    public void FirebaseDocumentLiveDataTest_onInactive() {
        FirebaseDocumentLiveData FDLD = new FirebaseDocumentLiveData(mDocumentReference,String.class);

        Map<String, Object> mMapStringObj2 = new HashMap<>();
        mMapStringObj2.put("England", "London");
        when(mDocumentReference.addSnapshotListener(mDocumentSnapshotCompleteListenerCaptor.capture())).thenReturn(null);
        when(mDocumentSnapshot.getData()).thenReturn( null);

        FDLD.setListener(mListenerRegistration);
        FDLD.onInactive();
        //when(mListenerRegistration.remove()).then(any());
    }

}
