package ch.epfl.sdp.db.queries;

import androidx.annotation.NonNull;

import androidx.lifecycle.LiveData;
import ch.epfl.sdp.db.DatabaseObjectBuilder;

/**
 * A document is contained inside a collection {@link ch.epfl.sdp.db.queries.CollectionQuery} this is
 * the interface that each Document should implement.
 */
public interface DocumentQuery extends Query {

    /**
     * To query the parent collection.
     *
     * @param collection reference to the parent collection
     * @return query {@link ch.epfl.sdp.db.queries.CollectionQuery} object associated
     * to the reference argument
     */
    CollectionQuery collection(String collection);


    /**
     * Check if this query is associated to an existing document.
     *
     * @param callback class that will be called once the result is returned
     */
    void exists(@NonNull OnQueryCompleteCallback<Boolean> callback);

    /**
     * Get the object associated to this query.
     *
     * @param type class type that should be returned
     * @param callback class that will be called once the result is returned
     * @param <T> type that should be returned
     */
    <T> void get(@NonNull Class<T> type, @NonNull OnQueryCompleteCallback<T> callback);

    /**
     * Set or replace the object associated to this query.
     *
     * @param callback class that will be called once created
     * @param <T> type that should be returned
     */
    <T> void set(@NonNull T object, @NonNull OnQueryCompleteCallback<Void> callback);

    /**
     * Update a field of the object associated to this query.
     *
     * @param field target field to update
     * @param value object that should replace the targeted field
     * @param callback class that will be called once updated
     */
    void update(@NonNull String field, @NonNull Object value, @NonNull OnQueryCompleteCallback<Void> callback);

    /**
     * Get the livedata of the object associated to this query.
     *
     * @param type class type that should be returned
     * @param <T> type that should be returned
     */
    <T> LiveData<T> liveData(@NonNull Class<T> type);

    /**
     * Delete the object associated to this query.
     *
     * @param callback class that will be called once deleted
     */
    void delete(@NonNull OnQueryCompleteCallback<Void> callback);
}
