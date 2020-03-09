package ch.epfl.sdp.db.queries;

import androidx.annotation.NonNull;

import ch.epfl.sdp.db.DatabaseObjectBuilder;

public interface DocumentQuery extends Query {

    CollectionQuery collection(String collection);

    <T, B extends DatabaseObjectBuilder<T>> void get(@NonNull B builder, @NonNull OnQueryCompleteCallback<T> callback);

    void delete(@NonNull OnQueryCompleteCallback<Void> callback);
}
