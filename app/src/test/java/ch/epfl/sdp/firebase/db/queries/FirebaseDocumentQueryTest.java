package ch.epfl.sdp.firebase.db.queries;
import android.hardware.camera2.CameraCaptureSession;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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

import java.util.HashMap;
import java.util.Map;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.db.DatabaseObjectBuilder;
import ch.epfl.sdp.db.DatabaseObjectBuilderFactory;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.DocumentQuery;
import ch.epfl.sdp.db.queries.FilterQuery;
import ch.epfl.sdp.firebase.db.FirestoreDatabase;
import ch.epfl.sdp.firebase.db.MockStringBuilder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FirebaseDocumentQueryTest {

    @Mock
    private FirebaseFirestore mDb;

    @Mock
    private DocumentQuery mDocumentQuery;

    @Mock
    private DocumentReference mDocumentReference;

    @Mock
    private Task<DocumentSnapshot> mDocumentSnapshotTask;

    @Mock
    private DocumentSnapshot mDocumentSnapshot;

    @Mock
    Task<Void> mQuerySnapshotTask;

    @Mock
    private CollectionReference mCollectionReference;

    @Captor
    private ArgumentCaptor<OnCompleteListener<DocumentSnapshot>> mDocumentSnapshotCompleteListenerCaptor;

    @Captor
    private ArgumentCaptor<OnCompleteListener> mSnapshotCompleteListenerCaptor;

    private final static String DUMMY_STRING = "test";

    @Before
    public void setup() throws IllegalAccessException, InstantiationException {
        MockitoAnnotations.initMocks(this);
        DatabaseObjectBuilderFactory.clear();
        DatabaseObjectBuilderFactory.registerBuilder(String.class, MockStringBuilder.class);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseDocumentQuery_Constructor_FailsWithNullArgument() {
        FirestoreDatabase db = new FirestoreDatabase(mDb);
        FirebaseDocumentQuery FDQ = new FirebaseDocumentQuery(mDb,null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseDocumentQuery_collection_FailsWithNullArgument() {
        FirestoreDatabase db = new FirestoreDatabase(mDb);
        FirebaseDocumentQuery FDQ = new FirebaseDocumentQuery(mDb,mDocumentReference);
        FDQ.collection(null);
    }

    @Test
    public void FirebaseDocumentQuery_get_CallsCallbackWithDeserializedObject() {
        when(mDocumentSnapshotTask.addOnCompleteListener(mDocumentSnapshotCompleteListenerCaptor.capture())).thenReturn(null);
        when(mDocumentSnapshotTask.isSuccessful()).thenReturn(true);
        when(mDocumentSnapshot.exists()).thenReturn(true);
        when(mDocumentSnapshot.getData()).thenReturn(DatabaseObjectBuilderFactory.getBuilder(String.class).serializeToMap(DUMMY_STRING));
        when(mDocumentSnapshotTask.getResult()).thenReturn(mDocumentSnapshot);
        when(mDocumentReference.get()).thenReturn(mDocumentSnapshotTask);

        FirebaseDocumentQuery query = new FirebaseDocumentQuery(mDb, mDocumentReference);
        query.get(String.class, s -> {
            assertEquals(DUMMY_STRING, s.getData());
        });

        mDocumentSnapshotCompleteListenerCaptor.getValue().onComplete(mDocumentSnapshotTask);
    }


    @Test
    public void FirebaseDocumentQuery_collection() {
        when(mDocumentReference.collection("text")).thenReturn(mCollectionReference);

        FirebaseDocumentQuery FDQ = new FirebaseDocumentQuery(mDb,mDocumentReference);
        assertNotNull(FDQ.collection("text"));
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseDocumentQuery_livedata_FailsWithNullArgument() {
        FirestoreDatabase db = new FirestoreDatabase(mDb);
        FirebaseDocumentQuery FDQ = new FirebaseDocumentQuery(mDb,mDocumentReference);
        FDQ.livedata(null);
    }

    //i don't know how to do it
    @Test
    public void FirebaseDocumentQuery_livedata() {
        FirestoreDatabase db = new FirestoreDatabase(mDb);
        FirebaseDocumentQuery FDQ = new FirebaseDocumentQuery(mDb,mDocumentReference);
        LiveData<Event> test = FDQ.livedata(Event.class);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseDocumentQuery_get_FailsWithNullArgument() {
        FirebaseDocumentQuery FDQ = new FirebaseDocumentQuery(mDb,mDocumentReference);
        FDQ.get(null,null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseDocumentQuery_delete_FailsWithNullArgument() {
        FirestoreDatabase db = new FirestoreDatabase(mDb);
        FirebaseDocumentQuery FDQ = new FirebaseDocumentQuery(mDb,mDocumentReference);
        FDQ.delete(null);
    }


    @Test
    public void FirebaseDocumentQuery_delete_success() {
        FirebaseDocumentQuery FDQ = new FirebaseDocumentQuery(mDb,mDocumentReference);

        when(mDocumentReference.delete()).thenReturn(mQuerySnapshotTask);
        when(mQuerySnapshotTask.addOnCompleteListener(mSnapshotCompleteListenerCaptor.capture())).thenReturn(null);
        when(mQuerySnapshotTask.isSuccessful()).thenReturn(true);
        when(mQuerySnapshotTask.getResult()).thenReturn(null);
        FDQ.delete(result -> {
            assertNull(result.getData());
        });

        mSnapshotCompleteListenerCaptor.getValue().onComplete(mQuerySnapshotTask);
    }

    @Test
    public void FirebaseDocumentQuery_delete_notSuccess() {
        FirebaseDocumentQuery FDQ = new FirebaseDocumentQuery(mDb,mDocumentReference);

        Exception exception = new Exception("exception");

        when(mDocumentReference.delete()).thenReturn(mQuerySnapshotTask);
        when(mQuerySnapshotTask.addOnCompleteListener(mSnapshotCompleteListenerCaptor.capture())).thenReturn(null);
        when(mQuerySnapshotTask.isCanceled()).thenReturn(true);
        //when(mQuerySnapshotTask.getResult()).thenReturn(exception);
        when(mQuerySnapshotTask.getException()).thenReturn(exception);

        FDQ.delete(result -> {
            assertEquals(exception, result.getException());
        });

        mSnapshotCompleteListenerCaptor.getValue().onComplete(mQuerySnapshotTask);
    }

}
