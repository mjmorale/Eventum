package ch.epfl.sdp.db.queries;

import androidx.annotation.NonNull;

import androidx.lifecycle.LiveData;
import ch.epfl.sdp.future.Future;

public interface DocumentQuery {

    CollectionQuery collection(@NonNull String collection);

    Future<Boolean> exists();

    <T> Future<T> get(@NonNull Class<T> type);

    <T> Future<Void> set(@NonNull T object);

    Future<Void> update(@NonNull String field, @NonNull Object value);

    <T> LiveData<T> livedata(@NonNull Class<T> type);

    Future<Void> delete();
}
