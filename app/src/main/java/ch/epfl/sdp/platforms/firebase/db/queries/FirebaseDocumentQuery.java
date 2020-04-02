package ch.epfl.sdp.platforms.firebase.db.queries;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.lifecycle.LiveData;
import ch.epfl.sdp.db.DatabaseObjectBuilder;
import ch.epfl.sdp.db.DatabaseObjectBuilderRegistry;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.DocumentQuery;
import ch.epfl.sdp.db.queries.QueryResult;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class FirebaseDocumentQuery extends FirebaseQuery implements DocumentQuery {

    private final DocumentReference mDocument;

    FirebaseDocumentQuery(@NonNull FirebaseFirestore database, @NonNull DocumentReference document) {
        super(database);

        mDocument = verifyNotNull(document);
    }

    @Override
    public CollectionQuery collection(@NonNull String collection) {
        return new FirebaseCollectionQuery(mDb, mDocument.collection(verifyNotNull(collection)));
    }

    @Override
    public void exists(@NonNull OnQueryCompleteCallback<Boolean> callback) {
        verifyNotNull(callback);
        documentPerformAction(mDocument.get(), task -> {
            if(task.isSuccessful()) {
                DocumentSnapshot doc = task.getResult();
                callback.onQueryComplete(QueryResult.success(doc.exists()));
            }
            else {
                callback.onQueryComplete(QueryResult.failure(task.getException()));
            }
        });
    }

    @Override
    public <T> void get(@NonNull Class<T> type, @NonNull OnQueryCompleteCallback<T> callback) {
        verifyNotNull(type, callback);
        handleDocumentSnapshot(mDocument.get(), type, callback);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void set(@NonNull T object, @NonNull OnQueryCompleteCallback<Void> callback) {
        verifyNotNull(object, callback);
        DatabaseObjectBuilder<T> builder = DatabaseObjectBuilderRegistry.getBuilder((Class<T>)object.getClass());
        documentPerformAction(mDocument.set(builder.serializeToMap(object)), task -> {
            if(task.isSuccessful()) {
                callback.onQueryComplete(QueryResult.success(null));
            }
            else {
                callback.onQueryComplete(QueryResult.failure(task.getException()));
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> LiveData<T> livedata(@NonNull Class<T> type) {
        return new FirebaseDocumentLiveData(mDocument, type);
    }

    @Override
    public void delete(@NonNull OnQueryCompleteCallback<Void> callback) {
        verifyNotNull(callback);
        documentPerformAction(mDocument.delete(), task -> {
            if(task.isSuccessful()) {
                callback.onQueryComplete(QueryResult.success(null));
            } else {
                callback.onQueryComplete(QueryResult.failure(task.getException()));
            }
        });
    }

    private <T> void documentPerformAction(@NonNull Task<T> queryTask, @NonNull OnCompleteListener<T> completeListener) {
        queryTask.addOnCompleteListener(completeListener);
    }
}
