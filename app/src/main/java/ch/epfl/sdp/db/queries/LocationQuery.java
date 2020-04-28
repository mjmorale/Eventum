package ch.epfl.sdp.db.queries;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import ch.epfl.sdp.db.DatabaseObject;

import java.util.Collection;
import java.util.List;

public interface LocationQuery extends Query {

    <T> void get(@NonNull Class<T> type, @NonNull OnQueryCompleteCallback<List<DatabaseObject<T>>> callback);

    <T> LiveData<Collection<DatabaseObject<T>>> liveData(@NonNull Class<T> type);
}
