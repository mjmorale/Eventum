package ch.epfl.sdp.db.queries;

import androidx.annotation.NonNull;

import java.util.List;

import androidx.lifecycle.LiveData;
import ch.epfl.sdp.db.DatabaseObject;

/**
 * A particular query that allows different filtering of results returned.
 */
public interface FilterQuery extends Query {

    /**
     * Filter query that returns filtered objects based on field equality.
     *
     * @param field target field to be filtered
     * @param value targeted value
     * @return query {@link ch.epfl.sdp.db.queries.FilterQuery} object associated to the filtered
     * query
     */
    FilterQuery whereFieldEqualTo(@NonNull String field, Object value);

    /**
     * Filter query that returns filtered objects that contains a certain object
     * in an array field.
     *
     * @param field The name of the array field
     * @param value The targeted value
     * @return query {@link ch.epfl.sdp.db.queries.FilterQuery} object associated to the filtered
     * query
     */
    FilterQuery whereArrayContains(@NonNull String field, Object value);

    /**
     * Filter query that returns filtered objects based on field ordering.
     *
     * @param field target field to be ordered
     * @return query {@link ch.epfl.sdp.db.queries.FilterQuery} object associated to the filtered
     * query
     */
    FilterQuery orderBy(@NonNull String field);

    /**
     * Filter query that returns a limited number of objects.
     *
     * @param count target value
     * @return query {@link ch.epfl.sdp.db.queries.FilterQuery} object associated to the filtered
     * query
     */
    FilterQuery limitCount(int count);

    /**
     * Get the object associated to this query.
     *
     * @param type class type that should be returned
     * @param callback class that will be called once the result is returned
     * @param <T> type that should be returned
     */
    <T> void get(@NonNull Class<T> type, @NonNull Query.OnQueryCompleteCallback<List<DatabaseObject<T>>> callback);

    /**
     * Get the livedata the object associated to this query.
     *
     * @param type class type that should be returned
     * @param <T> type that should be returned
     */
    <T> LiveData<List<DatabaseObject<T>>> liveData(@NonNull Class<T> type);
}
