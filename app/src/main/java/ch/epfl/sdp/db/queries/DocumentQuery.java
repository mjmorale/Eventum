package ch.epfl.sdp.db.queries;

import androidx.annotation.NonNull;

import androidx.lifecycle.LiveData;
import ch.epfl.sdp.db.DatabaseObjectBuilder;

public interface DocumentQuery extends Query {

    CollectionQuery collection(String collection);

    <T> void get(@NonNull Class<T> type, @NonNull OnQueryCompleteCallback<T> callback);

    <T> LiveData<T> livedata(@NonNull Class<T> type);

    void delete(@NonNull OnQueryCompleteCallback<Void> callback);
}
