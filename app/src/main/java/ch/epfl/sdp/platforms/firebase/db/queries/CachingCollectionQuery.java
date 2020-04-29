package ch.epfl.sdp.platforms.firebase.db.queries;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.List;

import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.DocumentQuery;
import ch.epfl.sdp.db.queries.FilterQuery;
import ch.epfl.sdp.db.queries.LocationQuery;

public class CachingCollectionQuery implements CollectionQuery {
    private final FirebaseCollectionQuery mCollectionQuery;
    private final String mCollection;
    public CachingCollectionQuery(FirebaseFirestore mDb, String collection){
        mCollectionQuery = new FirebaseCollectionQuery(mDb, mDb.collection(collection));
        mCollection = collection;
    }

    @Override
    public DocumentQuery document(@NonNull String document) {
        if(mCollection != "Events")
            return mCollectionQuery.document(document);
        else {
            return null;
        }
    }

    @Override
    public FilterQuery whereFieldEqualTo(@NonNull String field, Object value) {
        return mCollectionQuery.whereFieldEqualTo(field, value);
    }

    @Override
    public FilterQuery orderBy(@NonNull String field) {
        return mCollectionQuery.orderBy(field);
    }

    @Override
    public FilterQuery limitCount(int count) {
        return mCollectionQuery.limitCount(count);
    }

    @Override
    public LocationQuery atLocation(GeoPoint location, double radius) {
        return mCollectionQuery.atLocation(location, radius);
    }

    @Override
    public <T> void get(@NonNull Class<T> type, @NonNull OnQueryCompleteCallback<List<T>> callback) {
        mCollectionQuery.get(type, callback);
    }

    @Override
    public <T> LiveData<List<T>> liveData(@NonNull Class<T> type) {
        return mCollectionQuery.liveData(type);
    }

    @Override
    public <T> void create(@NonNull T object, @NonNull OnQueryCompleteCallback<String> callback) {
        mCollectionQuery.create(object, callback);
    }
}
