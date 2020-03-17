package ch.epfl.sdp.firebase.db.queries;

import androidx.annotation.NonNull;
import ch.epfl.sdp.db.DatabaseObjectBuilder;
import ch.epfl.sdp.db.DatabaseObjectBuilderFactory;
import ch.epfl.sdp.db.queries.Query;
import ch.epfl.sdp.db.queries.QueryResult;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public abstract class FirebaseQuery {

    protected final FirebaseFirestore mDb;

    public FirebaseQuery(@NonNull FirebaseFirestore database) {
        if(database == null) {
            throw new IllegalArgumentException();
        }
        mDb = database;
    }

    protected <T> void handleDocumentSnapshot(@NonNull Task<DocumentSnapshot> task, @NonNull Class<T> type, @NonNull Query.OnQueryCompleteCallback callback) {
        if(task == null) {
            throw new IllegalArgumentException();
        }
        task.addOnCompleteListener(t -> {
            if(t.isSuccessful()) {
                DatabaseObjectBuilder<T> builder = DatabaseObjectBuilderFactory.getBuilder(type);
                DocumentSnapshot doc = t.getResult();
                T data = null;
                if(doc.exists()) {
                    data = builder.buildFromMap(doc.getData());
                }
                callback.onQueryComplete(QueryResult.success(data));
            }
            else {
                callback.onQueryComplete(QueryResult.failure(t.getException()));
            }
        });
    }

    protected <T> void handleQuerySnapshot(@NonNull Task<QuerySnapshot> task, @NonNull Class<T> type, @NonNull Query.OnQueryCompleteCallback callback) {
        if(task == null) {
            throw new IllegalArgumentException();
        }
        task.addOnCompleteListener(t -> {
            if(t.isSuccessful()) {
                DatabaseObjectBuilder<T> builder = DatabaseObjectBuilderFactory.getBuilder(type);
                List<DocumentSnapshot> docs = t.getResult().getDocuments();
                List<T> data = new ArrayList<>();
                for(DocumentSnapshot doc: docs) {
                    data.add(builder.buildFromMap(doc.getData()));
                }
                callback.onQueryComplete(QueryResult.success(data));
            }
            else {
                callback.onQueryComplete(QueryResult.failure(t.getException()));
            }
        });
    }
}
