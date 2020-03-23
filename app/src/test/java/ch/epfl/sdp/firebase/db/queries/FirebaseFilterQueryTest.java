package ch.epfl.sdp.firebase.db.queries;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

import java.util.ArrayList;
import java.util.Arrays;

import ch.epfl.sdp.db.DatabaseObjectBuilderRegistry;
import ch.epfl.sdp.utils.MockStringBuilder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FirebaseFilterQueryTest {

    private final static String[] DUMMY_STRINGS = {"test1", "test2", "test3"};
    private final static Exception DUMMY_EXCEPTION = new Exception();
    private final static Object DUMMY_OBJECT = new Object();
    private final static String DUMMY_STRING = "test";
    private final static int DUMMY_INT = 54;

    @Mock
    private FirebaseFirestore mDb;

    @Mock
    private Query mQuery;

    @Mock
    private Task<QuerySnapshot> mQuerySnapshotTask;

    @Mock
    private QuerySnapshot mQuerySnapshot;

    @Mock
    private DocumentSnapshot mDocumentSnapshot1;
    @Mock
    private DocumentSnapshot mDocumentSnapshot2;
    @Mock
    private DocumentSnapshot mDocumentSnapshot3;

    @Captor
    private ArgumentCaptor<OnCompleteListener<QuerySnapshot>> mQuerySnapshotCompleteListenerCaptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DatabaseObjectBuilderRegistry.registerBuilder(String.class, MockStringBuilder.class);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseFilterQuery_Constructor_FailsWithNullFirstArgument() {
        FirebaseFilterQuery firebaseFilterQuery = new FirebaseFilterQuery(null, mQuery);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseFilterQuery_Constructor_FailsWithNullSecondArgument() {
        FirebaseFilterQuery firebaseFilterQuery = new FirebaseFilterQuery(mDb, null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseFilterQuery_Get_FailsWithNullFirstArgument() {
        FirebaseFilterQuery firebaseFilterQuery = new FirebaseFilterQuery(mDb, mQuery);
        firebaseFilterQuery.get(null, result -> {});
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseFilterQuery_Get_FailsWithNullSecondArgument() {
        FirebaseFilterQuery firebaseFilterQuery = new FirebaseFilterQuery(mDb, mQuery);
        firebaseFilterQuery.get(String.class, null);
    }

    @Test
    public void FirebaseFilterQuery_Get_CallsCallbackWithDeserializedListOfObjects() {
        when(mQuerySnapshotTask.addOnCompleteListener(mQuerySnapshotCompleteListenerCaptor.capture())).thenReturn(null);
        when(mQuerySnapshotTask.isSuccessful()).thenReturn(true);
        when(mDocumentSnapshot1.getData()).thenReturn(DatabaseObjectBuilderRegistry.getBuilder(String.class).serializeToMap(DUMMY_STRINGS[0]));
        when(mDocumentSnapshot2.getData()).thenReturn(DatabaseObjectBuilderRegistry.getBuilder(String.class).serializeToMap(DUMMY_STRINGS[1]));
        when(mDocumentSnapshot3.getData()).thenReturn(DatabaseObjectBuilderRegistry.getBuilder(String.class).serializeToMap(DUMMY_STRINGS[2]));
        when(mQuerySnapshotTask.getResult()).thenReturn(mQuerySnapshot);
        when(mQuerySnapshot.getDocuments()).thenReturn(new ArrayList<>(Arrays.asList(mDocumentSnapshot1, mDocumentSnapshot2, mDocumentSnapshot3)));
        when(mQuery.get()).thenReturn(mQuerySnapshotTask);

        FirebaseFilterQuery firebaseFilterQuery = new FirebaseFilterQuery(mDb, mQuery);
        firebaseFilterQuery.get(String.class, s -> {
            assertTrue(s.isSuccessful());
            for(int i = 0; i < s.getData().size(); i++) {
                assertEquals(DUMMY_STRINGS[i], s.getData().get(i));
            }
        });

        mQuerySnapshotCompleteListenerCaptor.getValue().onComplete(mQuerySnapshotTask);
    }

    @Test
    public void FirebaseFilterQuery_Get_CallsCallbackWithEmptyListIfNoResults() {
        when(mQuerySnapshot.getDocuments()).thenReturn(new ArrayList<>());
        when(mQuerySnapshotTask.isSuccessful()).thenReturn(true);
        when(mQuerySnapshotTask.getResult()).thenReturn(mQuerySnapshot);
        when(mQuerySnapshotTask.addOnCompleteListener(mQuerySnapshotCompleteListenerCaptor.capture())).thenReturn(null);
        when(mQuery.get()).thenReturn(mQuerySnapshotTask);

        FirebaseFilterQuery firebaseFilterQuery = new FirebaseFilterQuery(mDb, mQuery);
        firebaseFilterQuery.get(String.class, result -> {
            assertTrue(result.isSuccessful());
            assertTrue(result.getData().isEmpty());
        });

        mQuerySnapshotCompleteListenerCaptor.getValue().onComplete(mQuerySnapshotTask);
    }

    @Test
    public void FirebaseFilterQuery_Get_CallsCallbackWithExceptionIfAnErrorOccurs() {
        when(mQuerySnapshotTask.isSuccessful()).thenReturn(false);
        when(mQuerySnapshotTask.getException()).thenReturn(DUMMY_EXCEPTION);
        when(mQuerySnapshotTask.addOnCompleteListener(mQuerySnapshotCompleteListenerCaptor.capture())).thenReturn(null);
        when(mQuery.get()).thenReturn(mQuerySnapshotTask);

        FirebaseFilterQuery firebaseFilterQuery = new FirebaseFilterQuery(mDb, mQuery);
        firebaseFilterQuery.get(String.class, result -> {
            assertFalse(result.isSuccessful());
            assertEquals(DUMMY_EXCEPTION, result.getException());
        });

        mQuerySnapshotCompleteListenerCaptor.getValue().onComplete(mQuerySnapshotTask);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseFilterQuery_WhereFieldEqualTo_FailsWithNullFirstArgument() {
        FirebaseFilterQuery firebaseFilterQuery = new FirebaseFilterQuery(mDb, mQuery);
        firebaseFilterQuery.whereFieldEqualTo(null, DUMMY_OBJECT);
    }

    @Test
    public void FirebaseFilterQuery_WhereFieldEqualTo_FiltersQueryWithCorrectParameters() {
        FirebaseFilterQuery firebaseFilterQuery = new FirebaseFilterQuery(mDb, mQuery);
        firebaseFilterQuery.whereFieldEqualTo(DUMMY_STRING, DUMMY_OBJECT);

        verify(mQuery).whereEqualTo(DUMMY_STRING, DUMMY_OBJECT);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseFilterQuery_OrderBy_FailsWithNullArgument() {
        FirebaseFilterQuery firebaseFilterQuery = new FirebaseFilterQuery(mDb, mQuery);
        firebaseFilterQuery.orderBy(null);
    }

    @Test
    public void FirebaseFilterQuery_OrderBy_OrdersQueryWithCorrectParameters() {
        FirebaseFilterQuery firebaseFilterQuery = new FirebaseFilterQuery(mDb, mQuery);
        firebaseFilterQuery.orderBy(DUMMY_STRING);

        verify(mQuery).orderBy(DUMMY_STRING);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseFilterQuery_LimitCount_FailsWithArgumentEqualToZero() {
        FirebaseFilterQuery firebaseFilterQuery = new FirebaseFilterQuery(mDb, mQuery);
        firebaseFilterQuery.limitCount(0);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseFilterQuery_LimitCount_FailsWithArgumentSmallerThanZero() {
        FirebaseFilterQuery firebaseFilterQuery = new FirebaseFilterQuery(mDb, mQuery);
        firebaseFilterQuery.limitCount(-1);
    }

    @Test
    public void FirebaseFilterQuery_LimitCount_LimitsQueryWithCorrectParameters() {
        FirebaseFilterQuery firebaseFilterQuery = new FirebaseFilterQuery(mDb, mQuery);
        firebaseFilterQuery.limitCount(DUMMY_INT);

        verify(mQuery).limit(DUMMY_INT);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseFilterQuery_Livedata_FailsWithNullArgument() {
        FirebaseFilterQuery firebaseFilterQuery = new FirebaseFilterQuery(mDb, mQuery);
        firebaseFilterQuery.livedata(null);
    }

    @Test
    public void FirebaseFilterQuery_Livedata_CreationOfLivedataDoesNotFail() {
        FirebaseFilterQuery firebaseFilterQuery = new FirebaseFilterQuery(mDb, mQuery);
        firebaseFilterQuery.livedata(String.class);
    }





}
