package ch.epfl.sdp.firebase.db.queries;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;

import androidx.lifecycle.LiveData;
import ch.epfl.sdp.db.DatabaseObjectBuilderFactory;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.DocumentQuery;
import ch.epfl.sdp.db.queries.FilterQuery;
import ch.epfl.sdp.db.queries.QueryResult;

public class FirebaseCollectionQuery extends FirebaseQuery implements CollectionQuery {

    private final CollectionReference mCollection;

    public FirebaseCollectionQuery(@NonNull FirebaseFirestore database, @NonNull CollectionReference collection) {
        super(database);
        if(collection == null) {
            throw new IllegalArgumentException();
        }
        mCollection = collection;
    }

    @Override
    public DocumentQuery document(@NonNull String document) {
        if(document == null) {
            throw new IllegalArgumentException();
        }
        return new FirebaseDocumentQuery(mDb, mCollection.document(document));
    }

    @Override
    public FilterQuery whereFieldEqualTo(@NonNull String field, Object value) {
        if(field == null) {
            throw new IllegalArgumentException();
        }
        return new FirebaseFilterQuery(mDb, mCollection.whereEqualTo(field, value));
    }

    @Override
    public FilterQuery orderBy(@NonNull String field) {
        if(field == null) {
            throw new IllegalArgumentException();
        }
        return new FirebaseFilterQuery(mDb, mCollection.orderBy(field));
    }

    @Override
    public FilterQuery limitCount(int count) {
        if(count <= 0) {
            throw new IllegalArgumentException();
        }
        return new FirebaseFilterQuery(mDb, mCollection.limit(count));
    }

    @Override
    public <T> void get(@NonNull Class<T> type, @NonNull OnQueryCompleteCallback<List<T>> callback) {
        if(type == null || callback == null) {
            throw new IllegalArgumentException();
        }
        handleQuerySnapshot(mCollection.get(), type, callback);
    }

    @Override
    public <T> LiveData<List<T>> livedata(@NonNull Class<T> type) {
        if(type == null) {
            throw new IllegalArgumentException();
        }
        return new FirebaseQueryLiveData(mCollection, type);
    }

    @Override
    public <T> void create(@NonNull T object, @NonNull OnQueryCompleteCallback<String> callback) {
        if(object == null || callback == null) {
            throw new IllegalArgumentException();
        }
        Map<String, Object> data = DatabaseObjectBuilderFactory.getBuilder((Class<T>) object.getClass()).serializeToMap(object);
        mCollection.add(data).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                callback.onQueryComplete(QueryResult.success(task.getResult().getId()));
            } else {
                callback.onQueryComplete(QueryResult.failure(task.getException()));
            }
        });
    }
}
