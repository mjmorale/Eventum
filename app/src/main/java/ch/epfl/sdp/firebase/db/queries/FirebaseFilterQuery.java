package ch.epfl.sdp.firebase.db.queries;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

import androidx.lifecycle.LiveData;
import ch.epfl.sdp.db.queries.FilterQuery;

public class FirebaseFilterQuery extends FirebaseQuery implements FilterQuery {

    private Query mQuery;

    FirebaseFilterQuery(@NonNull FirebaseFirestore database, @NonNull Query query) {
        super(database);
        if(query == null) {
            throw new IllegalArgumentException();
        }
        mQuery = query;
    }

    @Override
    public <T> void get(@NonNull Class<T> type, @NonNull OnQueryCompleteCallback<List<T>> callback) {
        if(type == null || callback == null) {
            throw new IllegalArgumentException();
        }
        handleQuerySnapshot(mQuery.get(), type, callback);
    }

    @Override
    public FilterQuery whereFieldEqualTo(@NonNull String field, Object value) {
        if(field == null) {
            throw new IllegalArgumentException();
        }
        mQuery = mQuery.whereEqualTo(field, value);
        return this;
    }

    @Override
    public FilterQuery orderBy(@NonNull String field) {
        if(field == null) {
            throw new IllegalArgumentException();
        }
        mQuery = mQuery.orderBy(field);
        return this;
    }

    @Override
    public FilterQuery limitCount(int count) {
        if(count <= 0) {
            throw new IllegalArgumentException();
        }
        mQuery = mQuery.limit(count);
        return this;
    }

    @Override
    public <T> LiveData<List<T>> livedata(@NonNull Class<T> type) {
        if(type == null) {
            throw new IllegalArgumentException();
        }
        return new FirebaseQueryLiveData(mQuery, type);
    }
}
