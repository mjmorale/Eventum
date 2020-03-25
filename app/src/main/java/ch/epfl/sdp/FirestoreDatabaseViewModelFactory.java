package ch.epfl.sdp;

import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import ch.epfl.sdp.firebase.db.FirestoreDatabase;

public class FirestoreDatabaseViewModelFactory extends DatabaseViewModelFactory {

    private static FirestoreDatabaseViewModelFactory mInstance;

    private FirestoreDatabaseViewModelFactory() {
        super(new FirestoreDatabase(FirebaseFirestore.getInstance()));
    }

    @NonNull
    public static FirestoreDatabaseViewModelFactory getInstance() {
        if(mInstance == null) {
            mInstance = new FirestoreDatabaseViewModelFactory();
        }
        return mInstance;
    }
}
