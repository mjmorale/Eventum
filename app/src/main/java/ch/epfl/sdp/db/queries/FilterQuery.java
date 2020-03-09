package ch.epfl.sdp.db.queries;

import androidx.annotation.NonNull;

import java.util.List;

import androidx.lifecycle.LiveData;
import ch.epfl.sdp.db.DatabaseObjectBuilder;

public interface FilterQuery extends Query {

    <T> void get(@NonNull Class<T> type, @NonNull OnQueryCompleteCallback<List<T>> callback);

    <T> LiveData<List<T>> livedata(@NonNull Class<T> type);
}
