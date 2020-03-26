package ch.epfl.sdp.firebase.db;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.FirebaseFirestore;

import org.imperiumlabs.geofirestore.GeoFirestore;

import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.firebase.db.queries.FirebaseCollectionQuery;
import ch.epfl.sdp.db.queries.CollectionQuery;

public class FirestoreDatabase implements Database {

    private final FirebaseFirestore mDb;

    public FirestoreDatabase(@NonNull FirebaseFirestore firestore) {
        if(firestore == null) {
            throw new IllegalArgumentException();
        }
        mDb = firestore;
    }

    @Override
    public CollectionQuery query(@NonNull String collection) {
        if(collection == null) {
            throw new IllegalArgumentException();
        }

        return new FirebaseCollectionQuery(mDb, mDb.collection(collection));
    }
}
