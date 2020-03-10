package ch.epfl.sdp.firebase.db.queries;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.lifecycle.LiveData;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.DocumentQuery;
import ch.epfl.sdp.db.queries.QueryResult;

public class FirebaseDocumentQuery extends FirebaseQuery implements DocumentQuery {

    private final DocumentReference mDocument;

    FirebaseDocumentQuery(@NonNull FirebaseFirestore database, @NonNull DocumentReference document) {
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
    public <T> void get(@NonNull Class<T> type, @NonNull OnQueryCompleteCallback<T> callback) {
        if(type == null || callback == null) {
            throw new IllegalArgumentException();
        }
        handleDocumentSnapshot(mDocument.get(), type, callback);
    }

    @Override
    public <T> LiveData<T> livedata(@NonNull Class<T> type) {
        return new FirebaseDocumentLiveData(mDocument, type);
    }

    @Override
    public void delete(@NonNull OnQueryCompleteCallback<Void> callback) {
        if(callback == null) {
            throw new IllegalArgumentException();
        }
        mDocument.delete().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                callback.onQueryComplete(QueryResult.success(null));
            } else {
                callback.onQueryComplete(QueryResult.failure(task.getException()));
            }
        });
    }
}
