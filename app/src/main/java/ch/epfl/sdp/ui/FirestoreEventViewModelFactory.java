package ch.epfl.sdp.ui;

import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.firebase.db.FirestoreDatabase;

public class FirestoreEventViewModelFactory extends EventViewModelFactory {

    private static Database sFirestoreDatabase;

    private FirestoreEventViewModelFactory(@NonNull Database database, @NonNull String eventRef) {
        super(database, eventRef);
    }

    public static FirestoreEventViewModelFactory getInstance(@NonNull String eventRef) {
        if(eventRef == null) {
            throw new IllegalArgumentException();
        }
        if(sFirestoreDatabase == null) {
            sFirestoreDatabase = new FirestoreDatabase(FirebaseFirestore.getInstance());
        }
        return new FirestoreEventViewModelFactory(sFirestoreDatabase, eventRef);
    }
}
