package ch.epfl.sdp.db.queries;

import androidx.annotation.NonNull;

import java.util.List;

import androidx.lifecycle.LiveData;
import ch.epfl.sdp.db.DatabaseObjectBuilder;

public interface CollectionQuery extends Query {

    DocumentQuery document(@NonNull String document);

    FilterQuery whereFieldEqualTo(@NonNull String field, Object value);

    <T, B extends DatabaseObjectBuilder<T>> void get(@NonNull B builder, @NonNull OnQueryCompleteCallback<List<T>> callback);

    <T, B extends DatabaseObjectBuilder<T>> LiveData<List<T>> livedata(@NonNull B builder);

    <T, B extends DatabaseObjectBuilder<T>> void create(@NonNull T object, @NonNull B builder, @NonNull OnQueryCompleteCallback<String> callback);
}
