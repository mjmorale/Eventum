package ch.epfl.sdp;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;

public class EventSwipeViewModel extends DatabaseViewModel {

    private final static String TAG = "EventSwipeViewModel";

    private final CollectionQuery mEventCollection;

    public EventSwipeViewModel(@NonNull Database database) {
        super(database);
        mEventCollection = database.query("events");
    }

    @NonNull
    public LiveData<List<Event>> getAll() {
        return mEventCollection.liveData(Event.class);
    }
}
