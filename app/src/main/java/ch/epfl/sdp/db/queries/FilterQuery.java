package ch.epfl.sdp.db.queries;

import androidx.annotation.NonNull;

import java.util.List;

import androidx.lifecycle.LiveData;
import ch.epfl.sdp.db.DatabaseObject;

public interface FilterQuery extends Query {

    FilterQuery whereFieldEqualTo(@NonNull String field, Object value);

    FilterQuery orderBy(@NonNull String field);

    FilterQuery limitCount(int count);

    <T> void get(@NonNull Class<T> type, @NonNull OnQueryCompleteCallback<List<DatabaseObject<T>>> callback);

    <T> LiveData<List<DatabaseObject<T>>> livedata(@NonNull Class<T> type);
}
