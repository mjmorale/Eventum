package ch.epfl.sdp.db.queries;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import ch.epfl.sdp.db.DatabaseObject;

import com.google.firebase.firestore.GeoPoint;

import java.util.Collection;
import java.util.List;

/**
 * A query returned by based location filtering methods like
 * {@link ch.epfl.sdp.db.queries.CollectionQuery#atLocation(GeoPoint, double)}.
 */
public interface LocationQuery extends Query {

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
    <T> LiveData<Collection<DatabaseObject<T>>> liveData(@NonNull Class<T> type);

}
