package ch.epfl.sdp.db.queries;

import androidx.annotation.NonNull;

import java.util.List;

import androidx.lifecycle.LiveData;
import ch.epfl.sdp.db.DatabaseObject;

import com.google.firebase.firestore.GeoPoint;

/**
 * A Collection is a list of documents {@link ch.epfl.sdp.db.queries.DocumentQuery} this is
 * the interface that each Collection should implement
 */
public interface CollectionQuery extends Query {

    /**
     * To query a document contained inside this collection.
     *
     * @param document reference to the desired document
     * @return query {@link ch.epfl.sdp.db.queries.DocumentQuery} object associated
     * to the reference argument
     */
    DocumentQuery document(@NonNull String document);

    /**
     * {@link ch.epfl.sdp.db.queries.FilterQuery#whereFieldEqualTo(String, Object)}
     */
    FilterQuery whereFieldEqualTo(@NonNull String field, Object value);

    /**
     * {@link ch.epfl.sdp.db.queries.FilterQuery#whereArrayContains(String, Object)}
     */
    FilterQuery whereArrayContains(@NonNull String field, Object value);

    /**
     * {@link ch.epfl.sdp.db.queries.FilterQuery#orderBy(String)}
     */
    FilterQuery orderBy(@NonNull String field);


    /**
     * {@link ch.epfl.sdp.db.queries.FilterQuery#limitCount(int)}
     */
    FilterQuery limitCount(int count);

    /**
     * Query that returns filtered events based on a radius centered at a location position.
     *
     * @param location target location
     * @param radius target radius
     * @return query {@link ch.epfl.sdp.db.queries.LocationQuery} object associated to the filtered
     * query
     */
    LocationQuery atLocation(GeoPoint location, double radius);

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

    /**
     * Create an object inside the collection associated to this query.
     *
     * @param object class that should put inside
     * @param callback class that will be called once the result is returned and return the
     *                 reference of the created object
     * @param <T> type that should be created
     */
    <T> void create(@NonNull T object, @NonNull Query.OnQueryCompleteCallback<String> callback);

}
