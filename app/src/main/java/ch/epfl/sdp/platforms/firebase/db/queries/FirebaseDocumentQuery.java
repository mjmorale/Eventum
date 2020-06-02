package ch.epfl.sdp.platforms.firebase.db.queries;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
        documentPerformAction(mDocument.get(), callback,
        success -> {
            callback.onQueryComplete(QueryResult.success(success.exists()));
        });
    }

    @Override
    public <T> void get(@NonNull Class<T> type, @NonNull OnQueryCompleteCallback<T> callback) {
        verifyNotNull(type, callback);
        handleDocumentSnapshot(mDocument.get(), type, callback);
    }

    @Override
    public void getField(@NonNull String field, @NonNull OnQueryCompleteCallback<Object> callback) {
        verifyNotNull(field, callback);
        mDocument.get().addOnCompleteListener(t -> {
            if(t.isSuccessful()) {
                DocumentSnapshot doc = t.getResult();
                Object data = null;
                if(doc.exists()) {
                    data = doc.get(field);
                }
                callback.onQueryComplete(QueryResult.success(data));
            }
            else {
                callback.onQueryComplete(QueryResult.failure(t.getException()));
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void set(@NonNull T object, @NonNull OnQueryCompleteCallback<Void> callback) {
        verifyNotNull(object, callback);
        DatabaseObjectBuilder<T> builder = DatabaseObjectBuilderRegistry.getBuilder((Class<T>)object.getClass());
        documentPerformAction(mDocument.set(builder.serializeToMap(object)), callback,
        success -> {
            callback.onQueryComplete(QueryResult.success(null));
        });
    }

    @Override
    public void update(@NonNull String field, @NonNull Object value, @NonNull OnQueryCompleteCallback<Void> callback) {
        verifyNotNull(field, value, callback);
        documentPerformAction(mDocument.update(field, value), callback,
        success -> {
            callback.onQueryComplete(QueryResult.success(null));
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> LiveData<T> liveData(@NonNull Class<T> type) {
        return new FirebaseDocumentLiveData(mDocument, type);
    }

    @Override
    public void delete(@NonNull OnQueryCompleteCallback<Void> callback) {
        verifyNotNull(callback);
        documentPerformAction(mDocument.delete(), callback,
        success -> {
            callback.onQueryComplete(QueryResult.success(null));
        });
    }

    private <T, U> void documentPerformAction(@NonNull Task<T> queryTask, @NonNull OnQueryCompleteCallback<U> callback, @NonNull OnSuccessListener<T> onSuccess) {
        queryTask.addOnSuccessListener(onSuccess).addOnFailureListener(exception -> {
            callback.onQueryComplete(QueryResult.failure(exception));
        });
    }
}
