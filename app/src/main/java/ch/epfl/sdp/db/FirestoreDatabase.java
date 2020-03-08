package ch.epfl.sdp.db;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.auth.User;

public class FirestoreDatabase implements Database {

    private final FirebaseFirestore mDb;

    public FirestoreDatabase(FirebaseFirestore firestore) {
        if(firestore == null) {
            throw new IllegalArgumentException();
        }
        mDb = firestore;
    }

    @Override
    public void loadEvents(OnQueryCompleteCallback<Event> callback) {
        if(callback == null) {
            throw new IllegalArgumentException();
        }
        Task<QuerySnapshot> queryTask = mDb.collection("events")
                .orderBy("date")
                .get();
        queryTask.addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                EventDatabaseBuilder eventBuilder = new EventDatabaseBuilder();
                List<Event> events = new ArrayList<>();
                for(QueryDocumentSnapshot doc: task.getResult()) {
                    events.add(eventBuilder.instantiateFromDatabase(doc.getData()));
                }
                callback.onQueryComplete(DatabaseQueryResult.success(events));
            }
            else {
                callback.onQueryComplete(DatabaseQueryResult.failure(task.getException()));
            }
        });
    }

    @Override
    public void loadUser(String uid, OnQueryCompleteCallback<User> callback) {
        if(callback == null || uid == null) {
            throw new IllegalArgumentException();
        }
        Task<DocumentSnapshot> queryTask = mDb.collection("users")
                .document(uid)
                .get();
        queryTask.addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
            }
            else {
            }
        });
    }
}
