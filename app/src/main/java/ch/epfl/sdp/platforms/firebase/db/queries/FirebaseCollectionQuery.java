package ch.epfl.sdp.platforms.firebase.db.queries;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.List;
import java.util.Map;

import androidx.lifecycle.LiveData;

import org.imperiumlabs.geofirestore.GeoFirestore;

import ch.epfl.sdp.db.DatabaseObjectBuilder;
import ch.epfl.sdp.db.DatabaseObjectBuilderRegistry;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.DocumentQuery;
import ch.epfl.sdp.db.queries.FilterQuery;
import ch.epfl.sdp.db.queries.LocationQuery;
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
    public LocationQuery atLocation(@NonNull GeoPoint geoPoint, double radius) {
        return new FirebaseGeoFirestoreQuery(mDb, new GeoFirestore(mCollection), geoPoint, radius);
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

    @SuppressWarnings("unchecked")
    @Override
    public <T> void create(@NonNull T object, @NonNull OnQueryCompleteCallback<String> callback) {
        verifyNotNull(object, callback);
        DatabaseObjectBuilder<T> builder = DatabaseObjectBuilderRegistry.getBuilder((Class<T>) object.getClass());
        Map<String, Object> data = verifyNotNull(builder.serializeToMap(object));
        mCollection.add(data).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                String ref = task.getResult().getId();
                if(builder.hasLocation()) {
                    GeoFirestore geoFirestore = new GeoFirestore(mCollection);
                    geoFirestore.setLocation(ref, builder.getLocation(object), e -> {
                        if(e == null) {
                            callback.onQueryComplete(QueryResult.success(ref));
                        }
                        else {
                            callback.onQueryComplete(QueryResult.failure(e));
                        }
                    });
                }
                else {
                    callback.onQueryComplete(QueryResult.success(ref));
                }
            } else {
                callback.onQueryComplete(QueryResult.failure(task.getException()));
            }
        });
    }
}
