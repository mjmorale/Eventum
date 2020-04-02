package ch.epfl.sdp.platforms.firebase.db.queries;

import com.google.firebase.firestore.CollectionReference;
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

import java.util.Collection;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.db.DatabaseObjectBuilderRegistry;
import ch.epfl.sdp.db.queries.Query;
import ch.epfl.sdp.utils.MockStringBuilder;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class FirebaseGeoQuery {



    private final static double DUMMY_RADIUS = 12;

    private static final Class mClass = Event.class;

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
    private Query.OnQueryCompleteCallback mCallback;

    @Mock
    private GeoFirestoreLiveData mGeoFirestoreLiveData;

    @Mock
    private GeoQuery mGeoQuery;


    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = IllegalArgumentException.class)
    public void firebaseGeoQueryFailsNullFirebase(){
        FirebaseGeoFirestoreQuery firebaseGeoQuery = new FirebaseGeoFirestoreQuery(null, mGeoFirestore, mLocation, DUMMY_RADIUS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fireBaseGeoQueryFailsNullGeoQuery(){
        FirebaseGeoFirestoreQuery firebaseGeoQuery = new FirebaseGeoFirestoreQuery(mDb, null, mLocation, DUMMY_RADIUS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void firebaseGeoQueryFailsNullGeopoint(){
        FirebaseGeoFirestoreQuery firebaseGeoFirestoreQuery = new FirebaseGeoFirestoreQuery(mDb, mGeoFirestore, null, DUMMY_RADIUS);
    }

    @Test
    public void firebaseGeoQueryCorrectlyCallsGet(){

        FirebaseGeoFirestoreQuery firebaseGeoFirestoreQuery = new FirebaseGeoFirestoreQuery(mDb, mGeoFirestore, mLocation, DUMMY_RADIUS);

        firebaseGeoFirestoreQuery.get(Event.class, mCallback);


//        verify(mGeoFirestore).getAtLocation(DUMMY_LOCATION, DUMMY_RADIUS, (list, e) -> {
//            firebaseGeoFirestoreQuery.handleLocationQuerySnapshot(list, e, Event.class, mCallback);
//        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void firebaseGeoFirestoreQueryLiveDataFailsOnNullArgument(){
        FirebaseGeoFirestoreQuery firebaseGeoFirestoreQuery = new FirebaseGeoFirestoreQuery(mDb, mGeoFirestore, mLocation, DUMMY_RADIUS);
        firebaseGeoFirestoreQuery.liveData(null);
    }

    @Test
    public void firebaseGeoFirestoreQueryLiveDataReturnsLiveData(){
        FirebaseGeoFirestoreQuery firebaseGeoFirestoreQuery = new FirebaseGeoFirestoreQuery(mDb, mGeoFirestore, mLocation, DUMMY_RADIUS);
        when(mGeoFirestore.queryAtLocation(mLocation, DUMMY_RADIUS)).thenReturn(mGeoQuery);
        firebaseGeoFirestoreQuery.liveData(String.class);
    }

}
