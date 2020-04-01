package ch.epfl.sdp.platforms.firebase.db;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import ch.epfl.sdp.platforms.firebase.db.FirestoreDatabase;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FirestoreDatabaseTest {

    @Mock
    private FirebaseFirestore mDb;

    @Mock
    private CollectionReference mCollectionReference;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirestoreDatabase_Constructor_FailsWithNullArgument() {
        FirestoreDatabase db = new FirestoreDatabase(null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirestoreDatabase_Query_FailsWithNullArgument() {
        FirestoreDatabase db = new FirestoreDatabase(mDb);
        db.query(null);
    }

    @Test
    public void FirestoreDatabase_Query_CreateCollectionQueryWithCorrectName() {
        when(mDb.collection("test")).thenReturn(mCollectionReference);

        FirestoreDatabase db = new FirestoreDatabase(mDb);
        db.query("test");

        verify(mDb).collection("test");
    }
}
