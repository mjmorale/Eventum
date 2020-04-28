package ch.epfl.sdp.db.queries;

import androidx.annotation.NonNull;

import java.util.List;

import androidx.lifecycle.LiveData;
import ch.epfl.sdp.db.DatabaseObject;

import com.google.firebase.firestore.GeoPoint;

public interface CollectionQuery extends Query {

    DocumentQuery document(@NonNull String document);

    FilterQuery whereFieldEqualTo(@NonNull String field, Object value);

    FilterQuery whereArrayContains(@NonNull String field, Object value);

    FilterQuery orderBy(@NonNull String field);

    FilterQuery limitCount(int count);

    LocationQuery atLocation(GeoPoint location, double radius);

    <T> void get(@NonNull Class<T> type, @NonNull OnQueryCompleteCallback<List<DatabaseObject<T>>> callback);

    <T> LiveData<List<DatabaseObject<T>>> liveData(@NonNull Class<T> type);

    <T> void create(@NonNull T object, @NonNull OnQueryCompleteCallback<String> callback);

}
