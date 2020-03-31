package ch.epfl.sdp.platforms.firebase.db;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.FirebaseFirestore;

import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.platforms.firebase.db.queries.FirebaseCollectionQuery;
import ch.epfl.sdp.db.queries.CollectionQuery;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class FirestoreDatabase implements Database {

    private final FirebaseFirestore mDb;

    public FirestoreDatabase(@NonNull FirebaseFirestore firestore) {
        mDb = verifyNotNull(firestore);
    }

    @Override
    public CollectionQuery query(@NonNull String collection) {
        return new FirebaseCollectionQuery(mDb, mDb.collection(verifyNotNull(collection)));
    }
}
