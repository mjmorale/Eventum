package ch.epfl.sdp.firebase.db.queries;

import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import ch.epfl.sdp.db.DatabaseObjectBuilderRegistry;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.utils.MockStringBuilder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FirebaseDocumentQueryTest {

    private final static String DUMMY_STRING = "test";
    private final static Exception DUMMY_EXCEPTION = new Exception();

    @Mock
    private FirebaseFirestore mDb;

    @Mock
    private DocumentReference mDocumentReference;

    @Mock
    private Task<DocumentSnapshot> mDocumentSnapshotTask;

    @Mock
    private DocumentSnapshot mDocumentSnapshot;

    @Mock
    Task<Void> mVoidTask;

    @Mock
    private CollectionReference mCollectionReference;

    @Captor
    private ArgumentCaptor<OnCompleteListener<DocumentSnapshot>> mDocumentSnapshotCompleteListenerCaptor;

    @Captor
    private ArgumentCaptor<OnCompleteListener<Void>> mVoidCompleteListenerCaptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DatabaseObjectBuilderRegistry.registerBuilder(String.class, MockStringBuilder.class);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseDocumentQuery_Constructor_FailsWithNullFirstArgument() {
        FirebaseDocumentQuery firebaseDocumentQuery = new FirebaseDocumentQuery(null, mDocumentReference);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseDocumentQuery_Constructor_FailsWithNullSecondArgument() {
        FirebaseDocumentQuery firebaseDocumentQuery = new FirebaseDocumentQuery(mDb, null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseDocumentQuery_Collection_FailsWithNullArgument() {
        FirebaseDocumentQuery firebaseDocumentQuery = new FirebaseDocumentQuery(mDb, mDocumentReference);
        firebaseDocumentQuery.collection(null);
    }

    @Test
    public void FirebaseDocumentQuery_Collection_ReturnsCollectionQueryWithCorrectParameters() {
        when(mDocumentReference.collection(DUMMY_STRING)).thenReturn(mCollectionReference);
        when(mCollectionReference.orderBy(any(String.class))).thenReturn(mCollectionReference);

        FirebaseDocumentQuery firebaseDocumentQuery = new FirebaseDocumentQuery(mDb, mDocumentReference);
        CollectionQuery collectionQuery = firebaseDocumentQuery.collection(DUMMY_STRING);
        collectionQuery.orderBy(DUMMY_STRING);
        verify(mCollectionReference).orderBy(DUMMY_STRING);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseDocumentQuery_Get_FailsWithNullFirstArgument() {
        FirebaseDocumentQuery firebaseDocumentQuery = new FirebaseDocumentQuery(mDb, mDocumentReference);
        firebaseDocumentQuery.get(null, result -> {});
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseDocumentQuery_Get_FailsWithNullSecondArgument() {
        FirebaseDocumentQuery firebaseDocumentQuery = new FirebaseDocumentQuery(mDb, mDocumentReference);
        firebaseDocumentQuery.get(String.class, null);
    }

    @Test
    public void FirebaseDocumentQuery_Get_CallsCallbackWithDeserializedObject() {
        when(mDocumentSnapshotTask.addOnCompleteListener(mDocumentSnapshotCompleteListenerCaptor.capture())).thenReturn(null);
        when(mDocumentSnapshotTask.isSuccessful()).thenReturn(true);
        when(mDocumentSnapshot.exists()).thenReturn(true);
        when(mDocumentSnapshot.getData()).thenReturn(DatabaseObjectBuilderRegistry.getBuilder(String.class).serializeToMap(DUMMY_STRING));
        when(mDocumentSnapshotTask.getResult()).thenReturn(mDocumentSnapshot);
        when(mDocumentReference.get()).thenReturn(mDocumentSnapshotTask);

        FirebaseDocumentQuery query = new FirebaseDocumentQuery(mDb, mDocumentReference);
        query.get(String.class, s -> {
            assertTrue(s.isSuccessful());
            assertEquals(DUMMY_STRING, s.getData());
        });

        mDocumentSnapshotCompleteListenerCaptor.getValue().onComplete(mDocumentSnapshotTask);
    }

    @Test
    public void FirebaseDocumentQuery_Get_CallsCallbackWithNullValueIfDocumentDoesNotExist() {
        when(mDocumentSnapshotTask.addOnCompleteListener(mDocumentSnapshotCompleteListenerCaptor.capture())).thenReturn(null);
        when(mDocumentSnapshotTask.isSuccessful()).thenReturn(true);
        when(mDocumentSnapshot.exists()).thenReturn(false);
        when(mDocumentSnapshotTask.getResult()).thenReturn(mDocumentSnapshot);
        when(mDocumentReference.get()).thenReturn(mDocumentSnapshotTask);

        FirebaseDocumentQuery query = new FirebaseDocumentQuery(mDb, mDocumentReference);
        query.get(String.class, s -> {
            assertTrue(s.isSuccessful());
            assertNull(s.getData());
        });

        mDocumentSnapshotCompleteListenerCaptor.getValue().onComplete(mDocumentSnapshotTask);
    }

    @Test
    public void FirebaseDocumentQuery_Get_CallsCallbackWithExceptionIfErrorOccurred() {
        when(mDocumentSnapshotTask.addOnCompleteListener(mDocumentSnapshotCompleteListenerCaptor.capture())).thenReturn(null);
        when(mDocumentSnapshotTask.isSuccessful()).thenReturn(false);
        when(mDocumentSnapshotTask.getException()).thenReturn(DUMMY_EXCEPTION);
        when(mDocumentReference.get()).thenReturn(mDocumentSnapshotTask);

        FirebaseDocumentQuery query = new FirebaseDocumentQuery(mDb, mDocumentReference);
        query.get(String.class, s -> {
            assertFalse(s.isSuccessful());
            assertEquals(DUMMY_EXCEPTION, s.getException());
        });

        mDocumentSnapshotCompleteListenerCaptor.getValue().onComplete(mDocumentSnapshotTask);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseDocumentQuery_Livedata_FailsWithNullArgument() {
        FirebaseDocumentQuery firebaseDocumentQuery = new FirebaseDocumentQuery(mDb, mDocumentReference);
        firebaseDocumentQuery.livedata(null);
    }

    @Test
    public void FirebaseDocumentQuery_Livedata_CreationDoesNotFail() {
        FirebaseDocumentQuery firebaseDocumentQuery = new FirebaseDocumentQuery(mDb, mDocumentReference);
        LiveData<String> stringLiveData = firebaseDocumentQuery.livedata(String.class);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseDocumentQuery_Delete_FailsWithNullArgument() {
        FirebaseDocumentQuery firebaseDocumentQuery = new FirebaseDocumentQuery(mDb, mDocumentReference);
        firebaseDocumentQuery.delete(null);
    }

    @Test
    public void FirebaseDocumentQuery_Delete_CallsCallbackWithSuccessAndNullValue() {
        when(mDocumentReference.delete()).thenReturn(mVoidTask);
        when(mVoidTask.addOnCompleteListener(mVoidCompleteListenerCaptor.capture())).thenReturn(null);
        when(mVoidTask.isSuccessful()).thenReturn(true);

        FirebaseDocumentQuery firebaseDocumentQuery = new FirebaseDocumentQuery(mDb, mDocumentReference);
        firebaseDocumentQuery.delete(result -> {
            assertTrue(result.isSuccessful());
            assertNull(result.getData());
        });

        mVoidCompleteListenerCaptor.getValue().onComplete(mVoidTask);
    }

    @Test
    public void FirebaseDocumentQuery_Delete_CallsCallbackWithExceptionIfAnErrorOccurred() {
        when(mDocumentReference.delete()).thenReturn(mVoidTask);
        when(mVoidTask.addOnCompleteListener(mVoidCompleteListenerCaptor.capture())).thenReturn(null);
        when(mVoidTask.isSuccessful()).thenReturn(false);
        when(mVoidTask.getException()).thenReturn(DUMMY_EXCEPTION);

        FirebaseDocumentQuery firebaseDocumentQuery = new FirebaseDocumentQuery(mDb, mDocumentReference);
        firebaseDocumentQuery.delete(result -> {
            assertFalse(result.isSuccessful());
            assertEquals(DUMMY_EXCEPTION, result.getException());
        });

        mVoidCompleteListenerCaptor.getValue().onComplete(mVoidTask);
    }

}
