package ch.epfl.sdp.db.firebase.queries;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch.epfl.sdp.db.DatabaseObjectBuilder;
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
    public <T, B extends DatabaseObjectBuilder<T>> void get(@NonNull B builder, @NonNull OnQueryCompleteCallback<List<T>> callback) {
        if(builder == null || callback == null) {
            throw new IllegalArgumentException();
        }
        mCollection.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                List<T> data = new ArrayList<>();
                for(DocumentSnapshot doc: task.getResult()) {
                    data.add(builder.buildFromMap(doc.getData()));
                }
                callback.onGetQueryComplete(QueryResult.success(data));
            }
            else {
                callback.onGetQueryComplete(QueryResult.failure(task.getException()));
            }
        });
    }

    @Override
    public <T, B extends DatabaseObjectBuilder<T>> void create(@NonNull T object, @NonNull B builder, @NonNull OnQueryCompleteCallback<String> callback) {
        if(object == null || builder == null || callback == null) {
            throw new IllegalArgumentException();
        }
        Map<String, Object> data = builder.serializeToMap(object);
        mCollection.add(data).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                callback.onGetQueryComplete(QueryResult.success(task.getResult().getId()));
            } else {
                callback.onGetQueryComplete(QueryResult.failure(task.getException()));
            }
        });
    }
}
