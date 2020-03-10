package ch.epfl.sdp.firebase.db.queries;

import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.db.queries.FilterQuery;
import ch.epfl.sdp.firebase.db.FirestoreDatabase;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FirebaseFilterQueryTest {
    @Mock
    private FirebaseFirestore mDb;

    @Mock
    private Query mQuery;

    @Mock
    private FilterQuery mFilterQuery;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseFilterQuery_Constructor_FailsWithNullArgument() {
        FirestoreDatabase db = new FirestoreDatabase(mDb);
        FirebaseFilterQuery FFQ = new FirebaseFilterQuery(mDb,null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseFilterQuery_Get_FailsWithNullArgument() {
        FirestoreDatabase db = new FirestoreDatabase(mDb);
        FirebaseFilterQuery FFQ = new FirebaseFilterQuery(mDb,mQuery);
        FFQ.get(null,null);
    }

    /* Je sais pas faire ! aaaaaaaaaaaaaaaaaaaaah !
    @Test
    public void FirebaseFilterQuery_get() {
        when(mQuery.get()).thenReturn();

        FirestoreDatabase db = new FirestoreDatabase(mDb);
        FirebaseFilterQuery FFQ = new FirebaseFilterQuery(mDb,mQuery);

        FFQ.get(Event.class, result -> {
            assertTrue(result.isSuccessful());
        });
    }*/

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseFilterQuery_whereFieldEqualTo_FailsWithNullArgument() {
        FirestoreDatabase db = new FirestoreDatabase(mDb);
        FirebaseFilterQuery FFQ = new FirebaseFilterQuery(mDb,mQuery);
        FFQ.whereFieldEqualTo(null,null);
    }

    @Test
    public void FirebaseFilterQuery_whereFieldEqualTo() {
        when(mFilterQuery.whereFieldEqualTo("title","name title")).thenReturn(mFilterQuery);

        FirestoreDatabase db = new FirestoreDatabase(mDb);
        FirebaseFilterQuery FFQ = new FirebaseFilterQuery(mDb,mQuery);
        assertEquals(FFQ, FFQ.whereFieldEqualTo("title","name title"));
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseFilterQuery_orderBy_FailsWithNullArgument() {
        FirestoreDatabase db = new FirestoreDatabase(mDb);
        FirebaseFilterQuery FFQ = new FirebaseFilterQuery(mDb,mQuery);
        FFQ.orderBy(null);
    }

    @Test
    public void FirebaseFilterQuery_orderBy() {
        when(mFilterQuery.orderBy("title")).thenReturn(mFilterQuery);

        FirestoreDatabase db = new FirestoreDatabase(mDb);
        FirebaseFilterQuery FFQ = new FirebaseFilterQuery(mDb,mQuery);
        assertEquals(FFQ, FFQ.orderBy("title"));
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseFilterQuery_limitCount_FailsWithNullArgument() {
        FirestoreDatabase db = new FirestoreDatabase(mDb);
        FirebaseFilterQuery FFQ = new FirebaseFilterQuery(mDb,mQuery);
        FFQ.limitCount(-1);
    }

    @Test
    public void FirebaseFilterQuery_limitCount() {
        when(mFilterQuery.limitCount(1)).thenReturn(mFilterQuery);

        FirestoreDatabase db = new FirestoreDatabase(mDb);
        FirebaseFilterQuery FFQ = new FirebaseFilterQuery(mDb,mQuery);
        assertEquals(FFQ, FFQ.limitCount(1));
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseFilterQuery_livedata_FailsWithNullArgument() {
        FirestoreDatabase db = new FirestoreDatabase(mDb);
        FirebaseFilterQuery FFQ = new FirebaseFilterQuery(mDb,mQuery);
        FFQ.livedata(null);
    }

    @Test
    public void FirebaseFilterQuery_livedata() {
        FirestoreDatabase db = new FirestoreDatabase(mDb);
        FirebaseFilterQuery FFQ = new FirebaseFilterQuery(mDb,mQuery);
        FFQ.livedata(Event.class);
    }





}
