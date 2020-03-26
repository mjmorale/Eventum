package ch.epfl.sdp.firebase.db.queries;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.List;
import java.util.Map;

import androidx.lifecycle.LiveData;

import org.imperiumlabs.geofirestore.GeoFirestore;
import org.imperiumlabs.geofirestore.GeoFirestore.CompletionCallback;


import ch.epfl.sdp.db.DatabaseObjectBuilderRegistry;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.DocumentQuery;
import ch.epfl.sdp.db.queries.FilterQuery;
import ch.epfl.sdp.db.queries.GeoFirestoreQuery;
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
    public GeoFirestoreQuery queryAtLocation(GeoPoint geoPoint, double radius) {
        return null;
    }


    @Override
    public <T> void get(@NonNull Class<T> type, @NonNull OnQueryCompleteCallback<List<T>> callback) {
        if(type == null || callback == null) {
            throw new IllegalArgumentException();
        }
        handleQuerySnapshot(mCollection.get(), type, callback);
    }

    @Override
    public <T> LiveData<List<T>> liveData(@NonNull Class<T> type) {
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
        Map<String, Object> data = DatabaseObjectBuilderRegistry.getBuilder((Class<T>) object.getClass()).serializeToMap(object);
        mCollection.add(data).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                callback.onQueryComplete(QueryResult.success(task.getResult().getId()));
            } else {
                callback.onQueryComplete(QueryResult.failure(task.getException()));
            }
        });
    }

    @Override
    public <T> void createWithLocation(@NonNull T object, @NonNull GeoFirestore.CompletionCallback callback, @NonNull GeoPoint geoPoint) {
    // Null Object and null callback Checks will be done in create
        if(geoPoint == null)
            throw new IllegalArgumentException();
        create(object, new OnQueryCompleteCallback<String>() {
            @Override
            public void onQueryComplete(QueryResult<String> result) {
                GeoFirestore geoFirestore = new GeoFirestore(mCollection);
                geoFirestore.setLocation(result.getData(), new GeoPoint(46.5196535, 6.6322734), new CompletionCallback() {
                    @Override
                    public void onComplete(Exception e) {
                        if(e == null)
                        {
                            Log.i("Location Succ","Set location was done");
                        } else {
                            Log.i("Location Error","Error while setting location" + e.toString());
                        }
                    }
                });
            }
        });
    }
}
