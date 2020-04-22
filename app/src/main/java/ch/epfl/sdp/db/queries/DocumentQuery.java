package ch.epfl.sdp.db.queries;

import androidx.annotation.NonNull;

import androidx.lifecycle.LiveData;
import ch.epfl.sdp.db.DatabaseObjectBuilder;

public interface DocumentQuery extends Query {

    CollectionQuery collection(String collection);

    void exists(@NonNull OnQueryCompleteCallback<Boolean> callback);

    <T> void get(@NonNull Class<T> type, @NonNull OnQueryCompleteCallback<T> callback);

    <T> void set(@NonNull T object, @NonNull OnQueryCompleteCallback<Void> callback);

    void update(@NonNull String field, @NonNull Object value, @NonNull OnQueryCompleteCallback<Void> callback);

    <T> LiveData<T> livedata(@NonNull Class<T> type);

    void delete(@NonNull OnQueryCompleteCallback<Void> callback);
}
