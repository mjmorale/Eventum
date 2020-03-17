package ch.epfl.sdp;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import java.util.List;

import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.DocumentQuery;
import ch.epfl.sdp.db.queries.FilterQuery;
import ch.epfl.sdp.db.queries.QueryResult;

public class MockCollectionQuery implements CollectionQuery {
    private static final String REF_SUCCESS = "fake";
    @Override
    public DocumentQuery document(@NonNull String document) {
        return new MockDocumentQuery();
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
    public <T> void get(@NonNull Class<T> type, @NonNull OnQueryCompleteCallback<List<T>> callback) {

    }

    @Override
    public <T> LiveData<List<T>> liveData(@NonNull Class<T> type) {
        return null;
    }

    @Override
    public <T> void create(@NonNull T object, @NonNull OnQueryCompleteCallback<String> callback) {
        callback.onQueryComplete(QueryResult.success(REF_SUCCESS));
    }
}
