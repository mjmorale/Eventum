package ch.epfl.sdp.platforms.firebase.db;

import com.google.firebase.firestore.CollectionReference;

import org.imperiumlabs.geofirestore.GeoFirestore;

public class GeoFirestoreFactory {
    private static GeoFirestoreFactory INSTANCE;


    private GeoFirestoreFactory(){

    }

    public GeoFirestore createGeoFirestore(CollectionReference collectionReference){
        return new GeoFirestore(collectionReference);
    }

    public static GeoFirestoreFactory getInstance() {
        if(INSTANCE == null){
            INSTANCE = new GeoFirestoreFactory();
        }
        return INSTANCE;
    }
}
