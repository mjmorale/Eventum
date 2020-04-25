package ch.epfl.sdp.platforms.firebase.db.queries;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.EventBuilder;
import ch.epfl.sdp.db.DatabaseObjectBuilderRegistry;
import ch.epfl.sdp.db.queries.DocumentQuery;
import ch.epfl.sdp.db.queries.FilterQuery;
import ch.epfl.sdp.db.queries.LocationQuery;
import ch.epfl.sdp.future.Future;
import ch.epfl.sdp.utils.MockStringBuilder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FirebaseCollectionQueryTest {

    private final static String DUMMY_STRING = "test";
    private final static String[] DUMMY_STRINGS = {"test1", "test2", "test3"};
    private final static Object DUMMY_OBJECT = new Object();
    private final static Integer DUMMY_INT = 4;
    private final static Exception DUMMY_EXCEPTION = new Exception();
    private final static String DUMMY_ID = "slkdfjghsdflkjg354sadf45";
    private final static double DUMMY_DOUBLE = 42;
    private final static double DUMMY_LATITUDE = 3.234234;
    private final static double DUMMY_LONGITUDE = 12.534556345;

    @Mock
    private FirebaseFirestore mDb;

    @Mock
    private CollectionReference mCollectionReference;

    @Mock
    private DocumentReference mDocumentReference;

    @Mock
    private Task<QuerySnapshot> mQuerySnapshotTask;

    @Mock
    private Task<DocumentReference> mDocumentReferenceTask;

    @Mock
    private Task<String> mStringTask;

    @Mock
    private QuerySnapshot mQuerySnapshot;

    @Mock
    private DocumentSnapshot mDocumentSnapshot1;
    @Mock
    private DocumentSnapshot mDocumentSnapshot2;
    @Mock
    private DocumentSnapshot mDocumentSnapshot3;

    @Mock
    private Query mQuery;

    @Captor
    private ArgumentCaptor<Continuation<DocumentReference, Task<String>>> mDocToStringContinuationArgumentCaptor;

    @Captor
    private ArgumentCaptor<OnCompleteListener<QuerySnapshot>> mQuerySnapshotCompleteListenerCaptor;

    @Captor
    private ArgumentCaptor<OnCompleteListener<DocumentReference>> mDocumentReferenceCompleteListenerCaptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DatabaseObjectBuilderRegistry.registerBuilder(String.class, MockStringBuilder.class);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseCollectionQuery_Constructor_FailsWithNullFirstArgument() {
        FirebaseCollectionQuery firebaseCollectionQuery = new FirebaseCollectionQuery(null, mCollectionReference);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseCollectionQuery_Constructor_FailsWithNullSecondArgument() {
        FirebaseCollectionQuery firebaseCollectionQuery = new FirebaseCollectionQuery(mDb, null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseCollectionQuery_Document_FailsWithNullParameter() {
        FirebaseCollectionQuery firebaseCollectionQuery = new FirebaseCollectionQuery(mDb, mCollectionReference);
        firebaseCollectionQuery.document(null);
    }

    @Test
    public void FirebaseCollectionQuery_Document_ReturnsNewDocumentQueryWithCorrectRef() {
        when(mDocumentReference.collection(any(String.class))).thenReturn(mCollectionReference);
        when(mCollectionReference.document(any(String.class))).thenReturn(mDocumentReference);
        FirebaseCollectionQuery firebaseCollectionQuery = new FirebaseCollectionQuery(mDb, mCollectionReference);
        DocumentQuery documentQuery = firebaseCollectionQuery.document(DUMMY_STRING);
        documentQuery.collection(DUMMY_STRING);
        verify(mDocumentReference).collection(DUMMY_STRING);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseCollectionQuery_WhereFieldEqualTo_FailsWithNullFirstArgument() {
        FirebaseCollectionQuery firebaseCollectionQuery = new FirebaseCollectionQuery(mDb, mCollectionReference);
        firebaseCollectionQuery.whereFieldEqualTo(null, DUMMY_OBJECT);
    }

    @Test
    public void FirebaseCollectionQuery_WhereFieldEqualTo_ReturnsNewFilterQueryWithCorrectParameters() {
        when(mCollectionReference.whereEqualTo(any(String.class), any(Object.class))).thenReturn(mQuery);
        FirebaseCollectionQuery firebaseCollectionQuery = new FirebaseCollectionQuery(mDb, mCollectionReference);
        FilterQuery filterQuery = firebaseCollectionQuery.whereFieldEqualTo(DUMMY_STRING, DUMMY_OBJECT);
        verify(mCollectionReference).whereEqualTo(DUMMY_STRING, DUMMY_OBJECT);
        filterQuery.limitCount(1);
        verify(mQuery).limit(1);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseCollectionQuery_OrderBy_FailsWithNullFirstArgument() {
        FirebaseCollectionQuery firebaseCollectionQuery = new FirebaseCollectionQuery(mDb, mCollectionReference);
        firebaseCollectionQuery.orderBy(null);
    }

    @Test
    public void FirebaseCollectionQuery_OrderBy_ReturnsNewFilterQueryWithCorrectParameters() {
        when(mCollectionReference.orderBy(any(String.class))).thenReturn(mQuery);
        FirebaseCollectionQuery firebaseCollectionQuery = new FirebaseCollectionQuery(mDb, mCollectionReference);
        FilterQuery filterQuery = firebaseCollectionQuery.orderBy(DUMMY_STRING);
        verify(mCollectionReference).orderBy(DUMMY_STRING);
        filterQuery.limitCount(1);
        verify(mQuery).limit(1);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseCollectionQuery_LimitCount_FailsWithValueEqualToZero() {
        FirebaseCollectionQuery firebaseCollectionQuery = new FirebaseCollectionQuery(mDb, mCollectionReference);
        firebaseCollectionQuery.limitCount(0);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseCollectionQuery_LimitCount_FailsWithValueSmallerThenZero() {
        FirebaseCollectionQuery firebaseCollectionQuery = new FirebaseCollectionQuery(mDb, mCollectionReference);
        firebaseCollectionQuery.limitCount(-1);
    }

    @Test
    public void FirebaseCollectionQuery_LimitCount_ReturnsNewFilterQueryWithCorrectParameters() {
        when(mCollectionReference.limit(anyLong())).thenReturn(mQuery);

        FirebaseCollectionQuery firebaseCollectionQuery = new FirebaseCollectionQuery(mDb, mCollectionReference);
        firebaseCollectionQuery.limitCount(DUMMY_INT);

        verify(mCollectionReference).limit((long)DUMMY_INT);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseCollectionQuery_Get_FailsWithNullArgument() {
        FirebaseCollectionQuery firebaseCollectionQuery = new FirebaseCollectionQuery(mDb, mCollectionReference);
        firebaseCollectionQuery.get(null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseCollectionQuery_Livedata_FailsWithNullArgument() {
        FirebaseCollectionQuery firebaseCollectionQuery = new FirebaseCollectionQuery(mDb, mCollectionReference);
        firebaseCollectionQuery.liveData(null);
    }

    @Test
    public void FirebaseCollectionQuery_Livedata_CreationDoesNotFail() {
        FirebaseCollectionQuery firebaseCollectionQuery = new FirebaseCollectionQuery(mDb, mCollectionReference);
        LiveData<List<String>> stringsLiveData = firebaseCollectionQuery.liveData(String.class);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseCollectionQuery_Create_FailsWithNullFirstArgument() {
        FirebaseCollectionQuery firebaseCollectionQuery = new FirebaseCollectionQuery(mDb, mCollectionReference);
        firebaseCollectionQuery.create(null);
    }

    @Test
    public void FirebaseCollectionQuery_Create_ReturnsRefIsSuccessful() throws Exception {
        when(mCollectionReference.add(any())).thenReturn(mDocumentReferenceTask);
        when(mDocumentReferenceTask.continueWithTask(mDocToStringContinuationArgumentCaptor.capture())).thenReturn(mStringTask);
        when(mDocumentReferenceTask.getResult()).thenReturn(mDocumentReference);
        when(mDocumentReference.getId()).thenReturn(DUMMY_ID);

        FirebaseCollectionQuery firebaseCollectionQuery = new FirebaseCollectionQuery(mDb, mCollectionReference);
        firebaseCollectionQuery.create(DUMMY_STRING);

        assertEquals(DUMMY_ID, mDocToStringContinuationArgumentCaptor.getValue().then(mDocumentReferenceTask).getResult());
    }
}
