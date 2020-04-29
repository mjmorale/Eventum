package ch.epfl.sdp.platforms.firebase.db.queries;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.firebase.firestore.GeoPoint;

import java.util.List;

import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.DocumentQuery;
import ch.epfl.sdp.db.queries.FilterQuery;
import ch.epfl.sdp.db.queries.LocationQuery;

public class CachingCollectionQuery implements CollectionQuery {
    private final FirebaseCollectionQuery mCollectionQuery;

    public CachingCollectionQuery(FirebaseCollectionQuery firebaseCollectionQuery){
        mCollectionQuery = firebaseCollectionQuery;
    }

    @Override
    public DocumentQuery document(@NonNull String document) {
        return null;
    }

    @Override
    public FilterQuery whereFieldEqualTo(@NonNull String field, Object value) {
        return null;
    }

    @Override
    public FilterQuery orderBy(@NonNull String field) {
        return null;
    }

    @Override
    public FilterQuery limitCount(int count) {
        return null;
    }

    @Override
    public LocationQuery atLocation(GeoPoint location, double radius) {
        return null;
    }

    @Override
    public <T> void get(@NonNull Class<T> type, @NonNull OnQueryCompleteCallback<List<T>> callback) {

    }

    @Override
    public <T> LiveData<List<T>> liveData(@NonNull Class<T> type) {
        return null;
    }

    @Override
    public <T> void create(@NonNull T object, @NonNull OnQueryCompleteCallback<String> callback) {

    }
}
