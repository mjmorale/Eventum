package ch.epfl.sdp.firebase.db.queries;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.db.queries.FilterQuery;
import ch.epfl.sdp.firebase.db.FirestoreDatabase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FirebaseCollectionQueryTest {
    @Mock
    private FirebaseFirestore mDb;

    @Mock
    private CollectionReference mCollectionReference;

    @Mock
    private FilterQuery mFilterQuery;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseCollectionQuery_Constructor_FailsWithNullArgument() {
        FirestoreDatabase db = new FirestoreDatabase(mDb);
        FirebaseCollectionQuery FCQ = new FirebaseCollectionQuery(mDb,null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseCollectionQuery_Get_FailsWithNullArgument() {
        FirestoreDatabase db = new FirestoreDatabase(mDb);
        FirebaseCollectionQuery FCQ = new FirebaseCollectionQuery(mDb,mCollectionReference);
        FCQ.get(null,null);
    }

    @Test
    public void FirebaseCollectionQuery_get() {
    }


    @Test (expected = IllegalArgumentException.class)
    public void FirebaseCollectionQuery__whereFieldEqualTo_FailsWithNullArgument() {
        FirestoreDatabase db = new FirestoreDatabase(mDb);
        FirebaseCollectionQuery FCQ = new FirebaseCollectionQuery(mDb,mCollectionReference);
        FCQ.whereFieldEqualTo(null,null);
    }



    @Test (expected = IllegalArgumentException.class)
    public void FirebaseCollectionQuery_orderBy_FailsWithNullArgument() {
        FirestoreDatabase db = new FirestoreDatabase(mDb);
        FirebaseCollectionQuery FCQ = new FirebaseCollectionQuery(mDb,mCollectionReference);
        FCQ.orderBy(null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseCollectionQuery_limitCount_FailsWithNullArgument() {
        FirestoreDatabase db = new FirestoreDatabase(mDb);
        FirebaseCollectionQuery FCQ = new FirebaseCollectionQuery(mDb,mCollectionReference);
        FCQ.limitCount(-1);
    }


    @Test (expected = IllegalArgumentException.class)
    public void FirebaseCollectionQuery_livedata_FailsWithNullArgument() {
        FirestoreDatabase db = new FirestoreDatabase(mDb);
        FirebaseCollectionQuery FCQ = new FirebaseCollectionQuery(mDb,mCollectionReference);
        FCQ.livedata(null);
    }

    @Test
    public void FirebaseCollectionQuery_livedata() {
        FirestoreDatabase db = new FirestoreDatabase(mDb);
        FirebaseCollectionQuery FCQ = new FirebaseCollectionQuery(mDb,mCollectionReference);
        FCQ.livedata(Event.class);
    }


}
