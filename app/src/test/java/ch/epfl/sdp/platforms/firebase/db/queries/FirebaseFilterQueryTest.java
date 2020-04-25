package ch.epfl.sdp.platforms.firebase.db.queries;

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

import ch.epfl.sdp.db.DatabaseObjectBuilderRegistry;
import ch.epfl.sdp.utils.MockStringBuilder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

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
    public void FirebaseFilterQuery_Get_FailsWithNullArgument() {
        FirebaseFilterQuery firebaseFilterQuery = new FirebaseFilterQuery(mDb, mQuery);
        firebaseFilterQuery.get(null);
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
        firebaseFilterQuery.liveData(null);
    }

    @Test
    public void FirebaseFilterQuery_Livedata_CreationDoesNotFail() {
        FirebaseFilterQuery firebaseFilterQuery = new FirebaseFilterQuery(mDb, mQuery);
        firebaseFilterQuery.liveData(String.class);
    }

}
