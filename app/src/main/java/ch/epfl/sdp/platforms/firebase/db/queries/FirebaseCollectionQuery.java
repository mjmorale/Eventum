package ch.epfl.sdp.platforms.firebase.db.queries;

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
import org.imperiumlabs.geofirestore.GeoQuery;


import ch.epfl.sdp.db.DatabaseObjectBuilderRegistry;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.DocumentQuery;
import ch.epfl.sdp.db.queries.FilterQuery;
import ch.epfl.sdp.db.queries.GeoFirestoreQuery;
import ch.epfl.sdp.db.queries.QueryResult;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class FirebaseCollectionQuery extends FirebaseQuery implements CollectionQuery {

    private final CollectionReference mCollection;

    public FirebaseCollectionQuery(@NonNull FirebaseFirestore database, @NonNull CollectionReference collection) {
        super(database);

        mCollection = verifyNotNull(collection);
    }

    @Override
    public DocumentQuery document(@NonNull String document) {
        return new FirebaseDocumentQuery(mDb, mCollection.document(verifyNotNull(document)));
    }

    @Override
    public FilterQuery whereFieldEqualTo(@NonNull String field, Object value) {
        return new FirebaseFilterQuery(mDb, mCollection.whereEqualTo(verifyNotNull(field), value));
    }

    @Override
    public FilterQuery orderBy(@NonNull String field) {
        return new FirebaseFilterQuery(mDb, mCollection.orderBy(verifyNotNull(field)));
    }

    @Override
    public FilterQuery limitCount(int count) {
        if(count <= 0) {
            throw new IllegalArgumentException();
        }
        return new FirebaseFilterQuery(mDb, mCollection.limit(count));
    }

    @Override
    public GeoFirestoreQuery atLocation(GeoPoint geoPoint, double radius) {
        return new FirebaseGeoFirestoreQuery(mDb, new GeoFirestore(mCollection).queryAtLocation(geoPoint, radius));
    }


    @Override
    public <T> void get(@NonNull Class<T> type, @NonNull OnQueryCompleteCallback<List<T>> callback) {
        verifyNotNull(type, callback);
        handleQuerySnapshot(mCollection.get(), type, callback);
    }

    @Override
    public <T> LiveData<List<T>> liveData(@NonNull Class<T> type) {
        return new FirebaseQueryLiveData(mCollection, verifyNotNull(type));
    }

    @Override
    public <T> void create(@NonNull T object, @NonNull OnQueryCompleteCallback<String> callback) {
        verifyNotNull(object, callback);
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
