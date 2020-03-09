package ch.epfl.sdp.db.firebase.queries;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.FirebaseFirestore;

public abstract class FirebaseQuery {

    protected final FirebaseFirestore mDb;

    public FirebaseQuery(@NonNull FirebaseFirestore database) {
        if(database == null) {
            throw new IllegalArgumentException();
        }
        mDb = database;
    }
}
