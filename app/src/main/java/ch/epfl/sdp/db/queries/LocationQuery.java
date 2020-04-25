package ch.epfl.sdp.db.queries;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import ch.epfl.sdp.future.Future;

import java.util.List;

public interface LocationQuery {

    <T> Future<List<T>> get(@NonNull Class<T> type);

    <T> LiveData<List<T>> liveData(@NonNull Class<T> type);
}
