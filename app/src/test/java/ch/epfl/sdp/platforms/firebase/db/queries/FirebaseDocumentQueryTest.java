
package ch.epfl.sdp.platforms.firebase.db.queries;

import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import static org.mockito.ArgumentMatchers.anyString;
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
    private ArgumentCaptor<OnSuccessListener<DocumentSnapshot>> mDocumentSnapshotSuccessListenerCaptor;

    @Captor
    private ArgumentCaptor<OnFailureListener> mOnFailureListenerCaptor;

    @Captor
    private ArgumentCaptor<OnSuccessListener<Void>> mVoidSuccessListenerCaptor;

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
        firebaseDocumentQuery.liveData(null);
    }

    @Test
    public void FirebaseDocumentQuery_Livedata_CreationDoesNotFail() {
        FirebaseDocumentQuery firebaseDocumentQuery = new FirebaseDocumentQuery(mDb, mDocumentReference);
        LiveData<String> stringLiveData = firebaseDocumentQuery.liveData(String.class);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseDocumentQuery_Delete_FailsWithNullArgument() {
        FirebaseDocumentQuery firebaseDocumentQuery = new FirebaseDocumentQuery(mDb, mDocumentReference);
        firebaseDocumentQuery.delete(null);
    }

    @Test
    public void FirebaseDocumentQuery_Delete_CallsCallbackWithSuccessAndNullValue() {
        when(mDocumentReference.delete()).thenReturn(mVoidTask);
        when(mVoidTask.addOnSuccessListener(mVoidSuccessListenerCaptor.capture())).thenReturn(mVoidTask);
        when(mVoidTask.addOnFailureListener(any())).thenReturn(mVoidTask);

        FirebaseDocumentQuery firebaseDocumentQuery = new FirebaseDocumentQuery(mDb, mDocumentReference);
        firebaseDocumentQuery.delete(result -> {
            assertTrue(result.isSuccessful());
            assertNull(result.getData());
        });

        mVoidSuccessListenerCaptor.getValue().onSuccess(null);
    }

    @Test
    public void FirebaseDocumentQuery_Delete_CallsCallbackWithExceptionIfAnErrorOccurred() {
        when(mDocumentReference.delete()).thenReturn(mVoidTask);
        when(mVoidTask.addOnSuccessListener(any())).thenReturn(mVoidTask);
        when(mVoidTask.addOnFailureListener(mOnFailureListenerCaptor.capture())).thenReturn(mVoidTask);

        FirebaseDocumentQuery firebaseDocumentQuery = new FirebaseDocumentQuery(mDb, mDocumentReference);
        firebaseDocumentQuery.delete(result -> {
            assertFalse(result.isSuccessful());
            assertEquals(DUMMY_EXCEPTION, result.getException());
        });

        mOnFailureListenerCaptor.getValue().onFailure(DUMMY_EXCEPTION);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseDocumentQuery_Exists_FailsWithNullArgument() {
        FirebaseDocumentQuery firebaseDocumentQuery = new FirebaseDocumentQuery(mDb, mDocumentReference);
        firebaseDocumentQuery.exists(null);
    }

    @Test
    public void FirebaseDocumentQuery_Exists_ReturnsTrueIfDocumentExists() {
        when(mDocumentSnapshotTask.addOnSuccessListener(mDocumentSnapshotSuccessListenerCaptor.capture())).thenReturn(mDocumentSnapshotTask);
        when(mDocumentSnapshotTask.addOnFailureListener(mOnFailureListenerCaptor.capture())).thenReturn(mDocumentSnapshotTask);
        when(mDocumentReference.get()).thenReturn(mDocumentSnapshotTask);

        FirebaseDocumentQuery firebaseDocumentQuery = new FirebaseDocumentQuery(mDb, mDocumentReference);
        firebaseDocumentQuery.exists(result -> {
            assertTrue(result.isSuccessful());
            assertTrue(result.getData());
        });

        when(mDocumentSnapshot.exists()).thenReturn(true);
        mDocumentSnapshotSuccessListenerCaptor.getValue().onSuccess(mDocumentSnapshot);
    }

    @Test
    public void FirebaseDocumentQuery_Exists_ReturnsFalseIfDocumentDoesNotExist() {
        when(mDocumentSnapshotTask.addOnSuccessListener(mDocumentSnapshotSuccessListenerCaptor.capture())).thenReturn(mDocumentSnapshotTask);
        when(mDocumentSnapshotTask.addOnFailureListener(mOnFailureListenerCaptor.capture())).thenReturn(mDocumentSnapshotTask);
        when(mDocumentReference.get()).thenReturn(mDocumentSnapshotTask);

        FirebaseDocumentQuery firebaseDocumentQuery = new FirebaseDocumentQuery(mDb, mDocumentReference);
        firebaseDocumentQuery.exists(result -> {
            assertTrue(result.isSuccessful());
            assertFalse(result.getData());
        });

        when(mDocumentSnapshot.exists()).thenReturn(false);
        mDocumentSnapshotSuccessListenerCaptor.getValue().onSuccess(mDocumentSnapshot);
    }

    @Test
    public void FirebaseDocumentQuery_Exists_ReturnsAnExceptionIfFailure() {
        when(mDocumentSnapshotTask.addOnSuccessListener(mDocumentSnapshotSuccessListenerCaptor.capture())).thenReturn(mDocumentSnapshotTask);
        when(mDocumentSnapshotTask.addOnFailureListener(mOnFailureListenerCaptor.capture())).thenReturn(mDocumentSnapshotTask);
        when(mDocumentReference.get()).thenReturn(mDocumentSnapshotTask);

        FirebaseDocumentQuery firebaseDocumentQuery = new FirebaseDocumentQuery(mDb, mDocumentReference);
        firebaseDocumentQuery.exists(result -> {
            assertFalse(result.isSuccessful());
            assertEquals(DUMMY_EXCEPTION, result.getException());
        });

        mOnFailureListenerCaptor.getValue().onFailure(DUMMY_EXCEPTION);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseDocumentQuery_Set_FailsWithNullFirstArgument() {
        FirebaseDocumentQuery firebaseDocumentQuery = new FirebaseDocumentQuery(mDb, mDocumentReference);
        firebaseDocumentQuery.set(null, result -> {});
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseDocumentQuery_Set_FailsWithNullSecondArgument() {
        FirebaseDocumentQuery firebaseDocumentQuery = new FirebaseDocumentQuery(mDb, mDocumentReference);
        firebaseDocumentQuery.set(new Object(), null);
    }

    @Test
    public void FirebaseDocumentQuery_Set_ReturnsNoExceptionIfSuccessful() {
        when(mVoidTask.addOnSuccessListener(mVoidSuccessListenerCaptor.capture())).thenReturn(mVoidTask);
        when(mVoidTask.addOnFailureListener(mOnFailureListenerCaptor.capture())).thenReturn(mVoidTask);
        when(mDocumentReference.set(any())).thenReturn(mVoidTask);

        FirebaseDocumentQuery firebaseDocumentQuery = new FirebaseDocumentQuery(mDb, mDocumentReference);
        firebaseDocumentQuery.set(DUMMY_STRING, result -> {
            assertTrue(result.isSuccessful());
            assertNull(result.getData());
        });

        mVoidSuccessListenerCaptor.getValue().onSuccess(null);
    }

    @Test
    public void FirebaseDocumentQuery_Set_ReturnsWithExceptionIfFailure() {
        when(mVoidTask.addOnSuccessListener(mVoidSuccessListenerCaptor.capture())).thenReturn(mVoidTask);
        when(mVoidTask.addOnFailureListener(mOnFailureListenerCaptor.capture())).thenReturn(mVoidTask);
        when(mDocumentReference.set(any())).thenReturn(mVoidTask);

        FirebaseDocumentQuery firebaseDocumentQuery = new FirebaseDocumentQuery(mDb, mDocumentReference);
        firebaseDocumentQuery.set(DUMMY_STRING, result -> {
            assertFalse(result.isSuccessful());
            assertEquals(DUMMY_EXCEPTION, result.getException());
        });

        mOnFailureListenerCaptor.getValue().onFailure(DUMMY_EXCEPTION);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseDocumentQuery_Update_FailsWithNullFirstArgument() {
        FirebaseDocumentQuery firebaseDocumentQuery = new FirebaseDocumentQuery(mDb, mDocumentReference);
        firebaseDocumentQuery.update(null, new Object(), result -> {});
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseDocumentQuery_Update_FailsWithNullSecondArgument() {
        FirebaseDocumentQuery firebaseDocumentQuery = new FirebaseDocumentQuery(mDb, mDocumentReference);
        firebaseDocumentQuery.update("field", null, result -> {});
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseDocumentQuery_Update_FailsWithNullThirdArgument() {
        FirebaseDocumentQuery firebaseDocumentQuery = new FirebaseDocumentQuery(mDb, mDocumentReference);
        firebaseDocumentQuery.update("field", new Object(), null);
    }

    @Test
    public void FirebaseDocumentQuery_Update_ReturnsSuccessfullyIfSuccess() {
        when(mVoidTask.addOnSuccessListener(mVoidSuccessListenerCaptor.capture())).thenReturn(mVoidTask);
        when(mVoidTask.addOnFailureListener(mOnFailureListenerCaptor.capture())).thenReturn(mVoidTask);
        when(mDocumentReference.update(anyString(), any())).thenReturn(mVoidTask);

        FirebaseDocumentQuery firebaseDocumentQuery = new FirebaseDocumentQuery(mDb, mDocumentReference);
        firebaseDocumentQuery.update("field", new Object(), result -> {
            assertTrue(result.isSuccessful());
            assertNull(result.getData());
        });

        mVoidSuccessListenerCaptor.getValue().onSuccess(null);
    }

    @Test
    public void FirebaseDocumentQuery_Update_ReturnsAnExceptionIfFailure() {
        when(mVoidTask.addOnSuccessListener(mVoidSuccessListenerCaptor.capture())).thenReturn(mVoidTask);
        when(mVoidTask.addOnFailureListener(mOnFailureListenerCaptor.capture())).thenReturn(mVoidTask);
        when(mDocumentReference.update(anyString(), any())).thenReturn(mVoidTask);

        FirebaseDocumentQuery firebaseDocumentQuery = new FirebaseDocumentQuery(mDb, mDocumentReference);
        firebaseDocumentQuery.update("field", new Object(), result -> {
            assertFalse(result.isSuccessful());
            assertEquals(DUMMY_EXCEPTION, result.getException());
        });

        mOnFailureListenerCaptor.getValue().onFailure(DUMMY_EXCEPTION);
    }
}
