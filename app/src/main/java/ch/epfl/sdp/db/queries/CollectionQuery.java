package ch.epfl.sdp.db.queries;

import androidx.annotation.NonNull;

import java.util.List;

import androidx.lifecycle.LiveData;
import ch.epfl.sdp.db.DatabaseObjectBuilder;

public interface CollectionQuery extends Query {

    DocumentQuery document(@NonNull String document);

    FilterQuery whereFieldEqualTo(@NonNull String field, Object value);

    <T> void get(@NonNull Class<T> type, @NonNull OnQueryCompleteCallback<List<T>> callback);

    <T> LiveData<List<T>> livedata(@NonNull Class<T> type);

    <T> void create(@NonNull T object, @NonNull OnQueryCompleteCallback<String> callback);
}
