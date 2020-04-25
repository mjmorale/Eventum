package ch.epfl.sdp.db.queries;

import androidx.annotation.NonNull;

import java.util.List;

import androidx.lifecycle.LiveData;
import ch.epfl.sdp.future.Future;

public interface CollectionQuery {

    DocumentQuery document(@NonNull String document);

    FilterQuery whereFieldEqualTo(@NonNull String field, Object value);

    FilterQuery orderBy(@NonNull String field);

    FilterQuery limitCount(int count);

    LocationQuery atLocation(double latitude, double longitude, double radius);

    <T> Future<List<T>> get(@NonNull Class<T> type);

    <T> LiveData<List<T>> liveData(@NonNull Class<T> type);

    <T> Future<String> create(@NonNull T object);

}
