package ch.epfl.sdp.db.queries;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import java.util.Collection;
import java.util.List;

public interface LocationQuery extends Query {

    <T> void get(@NonNull Class<T> type, @NonNull OnQueryCompleteCallback<List<T>> callback);

    <T> LiveData<Collection<T>> liveData(@NonNull Class<T> type);
}
