package ch.epfl.sdp.platforms.firebase.db.queries;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Collection;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import ch.epfl.sdp.db.queries.FilterQuery;
import ch.epfl.sdp.future.Future;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class FirebaseFilterQuery extends FirebaseQuery implements FilterQuery {

    private Query mQuery;

    FirebaseFilterQuery(@NonNull FirebaseFirestore database, @NonNull Query query) {
        super(database);

        mQuery = verifyNotNull(query);
    }

    @Override
    public <T> Future<List<T>> get(@NonNull Class<T> type) {
        verifyNotNull(type);
        return handleQuerySnapshot(mQuery.get(), type);
    }

    @Override
    public FilterQuery whereFieldEqualTo(@NonNull String field, @Nullable Object value) {
        mQuery = mQuery.whereEqualTo(verifyNotNull(field), value);
        return this;
    }

    @Override
    public FilterQuery orderBy(@NonNull String field) {
        mQuery = mQuery.orderBy(verifyNotNull(field));
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

    @SuppressWarnings("unchecked")
    @Override
    public <T> LiveData<List<T>> liveData(@NonNull Class<T> type) {
        return new FirebaseQueryLiveData(mQuery, verifyNotNull(type));
    }
}
