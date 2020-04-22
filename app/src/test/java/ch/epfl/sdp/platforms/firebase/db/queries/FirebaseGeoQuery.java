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
import java.util.Collection;
import java.util.HashMap;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.db.DatabaseObjectBuilderRegistry;
import ch.epfl.sdp.db.queries.Query;
import ch.epfl.sdp.utils.MockStringBuilder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.doAnswer;
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

    @Mock
    private DocumentSnapshot mDocumentSnapshot;


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

    @Test
    public void firebaseGeoFirestoreQuery_Get_CallsCallback(){


        FirebaseGeoFirestoreQuery firebaseGeoFirestoreQuery = new FirebaseGeoFirestoreQuery(mDb, mGeoFirestore, mLocation, DUMMY_RADIUS);
        when(mDocumentSnapshot.getData()).thenReturn(
                new HashMap<String, Object>(){{
                    this.put("title", "title");
                    this.put("description", "description");
                    this.put("date", new Timestamp(20, 20));
                    this.put("address", "Chemin");
                    this.put("location", new GeoPoint(64, 65));
                    this.put("imageId", "URL");
                }}
        );
        ArrayList mDocuments = new ArrayList(){{this.add(mDocumentSnapshot); }};
        doAnswer(invocation -> {
             firebaseGeoFirestoreQuery.handleLocationQuerySnapshot(mDocuments, null, Event.class, result -> {mLocation.getLatitude();});
            return null;
        }).when(mGeoFirestore).getAtLocation(any(), anyDouble(), any());

        firebaseGeoFirestoreQuery.get(Event.class, result -> {
            firebaseGeoFirestoreQuery.handleLocationQuerySnapshot(mDocuments, null, Event.class, result1 -> { });
        });

        verify(mLocation).getLatitude();
    }
}
