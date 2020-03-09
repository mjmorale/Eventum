package ch.epfl.sdp.db.firebase.queries;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import ch.epfl.sdp.db.DatabaseObjectBuilder;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.DocumentQuery;
import ch.epfl.sdp.db.queries.QueryResult;

public class FirebaseDocumentQuery extends FirebaseQuery implements DocumentQuery {

    private final DocumentReference mDocument;

    public FirebaseDocumentQuery(@NonNull FirebaseFirestore database, @NonNull DocumentReference document) {
        super(database);
        if(document == null) {
            throw new IllegalArgumentException();
        }
        mDocument = document;
    }

    @Override
    public CollectionQuery collection(@NonNull String collection) {
        if(collection == null) {
            throw new IllegalArgumentException();
        }
        return new FirebaseCollectionQuery(mDb, mDocument.collection(collection));
    }

    @Override
    public <T, B extends DatabaseObjectBuilder<T>> void get(@NonNull B builder, @NonNull OnQueryCompleteCallback<T> callback) {
        if(builder == null || callback == null) {
            throw new IllegalArgumentException();
        }
        mDocument.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                DocumentSnapshot doc = task.getResult();
                T data = null;
                if(doc.exists()) {
                    data = builder.buildFromMap(doc.getData());
                }
                callback.onGetQueryComplete(QueryResult.success(data));
            } else {
                callback.onGetQueryComplete(QueryResult.failure(task.getException()));
            }
        });
    }

    @Override
    public void delete(@NonNull OnQueryCompleteCallback<Void> callback) {
        if(callback == null) {
            throw new IllegalArgumentException();
        }
        mDocument.delete().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                callback.onGetQueryComplete(QueryResult.success(null));
            } else {
                callback.onGetQueryComplete(QueryResult.failure(task.getException()));
            }
        });
    }
}
