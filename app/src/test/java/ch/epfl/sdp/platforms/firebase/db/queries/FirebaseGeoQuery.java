package ch.epfl.sdp.platforms.firebase.db.queries;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import org.imperiumlabs.geofirestore.GeoFirestore;
import org.imperiumlabs.geofirestore.GeoQuery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;

import ch.epfl.sdp.Event;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FirebaseGeoQuery {

    private final static double DUMMY_RADIUS = 12;

    @Mock
    private GeoPoint mLocation;

    @Mock
    private FirebaseFirestore mDb;

    @Mock
    private CollectionReference mCollectionReference;

    @Mock
    private FirebaseGeoFirestoreQuery mFirebaseGeoFirestoreQuery;

    @Mock
    private GeoFirestore mGeoFirestore;

    @Mock
    private GeoFirestoreLiveData mGeoFirestoreLiveData;

    @Mock
    private GeoQuery mGeoQuery;

    @Mock
    private DocumentSnapshot mDocumentSnapshot;


    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = IllegalArgumentException.class)
    public void FirebaseGeoQuery_Constructor_FailsNullFirebase() {
        FirebaseGeoFirestoreQuery firebaseGeoQuery = new FirebaseGeoFirestoreQuery(null, mGeoFirestore, mLocation, DUMMY_RADIUS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void FireBaseGeoQuery_Constructor_FailsNullGeoQuery() {
        FirebaseGeoFirestoreQuery firebaseGeoQuery = new FirebaseGeoFirestoreQuery(mDb, null, mLocation, DUMMY_RADIUS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void FirebaseGeoQuery_Constructor_FailsNullGeopoint() {
        FirebaseGeoFirestoreQuery firebaseGeoFirestoreQuery = new FirebaseGeoFirestoreQuery(mDb, mGeoFirestore, null, DUMMY_RADIUS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void FirebaseGeoFirestoreQuery_LiveData_FailsWithNullArgument() {
        FirebaseGeoFirestoreQuery firebaseGeoFirestoreQuery = new FirebaseGeoFirestoreQuery(mDb, mGeoFirestore, mLocation, DUMMY_RADIUS);
        firebaseGeoFirestoreQuery.liveData(null);
    }

    @Test
    public void FirebaseGeoFirestoreQuery_LiveData_CreateDoesNotFail() {
        FirebaseGeoFirestoreQuery firebaseGeoFirestoreQuery = new FirebaseGeoFirestoreQuery(mDb, mGeoFirestore, mLocation, DUMMY_RADIUS);
        when(mGeoFirestore.queryAtLocation(mLocation, DUMMY_RADIUS)).thenReturn(mGeoQuery);
        firebaseGeoFirestoreQuery.liveData(String.class);
    }
}
