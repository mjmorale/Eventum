package ch.epfl.sdp.db.queries;

import androidx.annotation.NonNull;

import androidx.lifecycle.LiveData;
import ch.epfl.sdp.db.DatabaseObjectBuilder;

public interface DocumentQuery extends Query {

    CollectionQuery collection(String collection);

    <T, B extends DatabaseObjectBuilder<T>> void get(@NonNull B builder, @NonNull OnQueryCompleteCallback<T> callback);

    <T, B extends DatabaseObjectBuilder<T>> LiveData<T> livedata(@NonNull B builder);

    void delete(@NonNull OnQueryCompleteCallback<Void> callback);
}
