package ch.epfl.sdp.platforms.firebase.db.queries;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import org.imperiumlabs.geofirestore.GeoQuery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import ch.epfl.sdp.db.DatabaseObjectBuilderRegistry;
import ch.epfl.sdp.platforms.firebase.db.queries.FirebaseGeoFirestoreQuery;
import ch.epfl.sdp.utils.MockStringBuilder;

@RunWith(MockitoJUnitRunner.class)
public class FirebaseGeoQuery {

    private final static GeoPoint DUMMY_LOCATION= new GeoPoint(12, 12);


    @Mock
    private FirebaseFirestore mDb;

    @Mock
    private CollectionReference mCollectionReference;

    @Mock
    private FirebaseGeoFirestoreQuery mFirebaseGeoFirestoreQuery;

    @Mock
    private GeoQuery mGeoQuery;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        DatabaseObjectBuilderRegistry.registerBuilder(String.class, MockStringBuilder.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void firebaseGeoQueryFailsNullFirebase(){
        FirebaseGeoFirestoreQuery firebaseGeoQuery = new FirebaseGeoFirestoreQuery(null, mGeoQuery);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fireBaseGeoQueryFailsNullGeoQuery(){
        FirebaseGeoFirestoreQuery firebaseGeoQuery = new FirebaseGeoFirestoreQuery(mDb, null);
    }

    @Test
    public void test(){
        FirebaseGeoFirestoreQuery firebaseGeoFirestoreQuery = new FirebaseGeoFirestoreQuery(mDb, mGeoQuery);
    }


}
