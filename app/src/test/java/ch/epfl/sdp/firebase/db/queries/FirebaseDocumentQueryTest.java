package ch.epfl.sdp.firebase.db.queries;
import androidx.lifecycle.LiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.DocumentQuery;
import ch.epfl.sdp.db.queries.FilterQuery;
import ch.epfl.sdp.firebase.db.FirestoreDatabase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
    private LiveData mLiveData;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
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

    /*
    @Test
    public void FirebaseDocumentQuery_collection() {
        when(mDocumentQuery.collection("text")).thenReturn(mCollectionQuery);
        FirestoreDatabase db = new FirestoreDatabase(mDb);
        FirebaseDocumentQuery FDQ = new FirebaseDocumentQuery(mDb,mDocumentReference);
        assertEquals(mDocumentReference, FDQ.collection("text"));
    }*/

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
    public void FirebaseDocumentQuery_delete_FailsWithNullArgument() {
        FirestoreDatabase db = new FirestoreDatabase(mDb);
        FirebaseDocumentQuery FDQ = new FirebaseDocumentQuery(mDb,mDocumentReference);
        FDQ.delete(null);
    }

    /*
    @Test
    public void FirebaseDocumentQuery_delete() {
        Exception exception = new Exception("dummy exception");
        FirestoreDatabase db = new FirestoreDatabase(mDb);
        FirebaseDocumentQuery FDQ = new FirebaseDocumentQuery(mDb,mDocumentReference);
        FDQ.delete(result -> {
            assertFalse(result.isSuccessful());
            assertEquals(exception, result.getException());
        });
    }*/

}
