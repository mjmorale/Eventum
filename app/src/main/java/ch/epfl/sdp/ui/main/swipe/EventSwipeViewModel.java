package ch.epfl.sdp.ui.main.swipe;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import ch.epfl.sdp.Event;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class EventSwipeViewModel extends ViewModel {

    private final static String TAG = "EventSwipeViewModel";

    private final CollectionQuery mEventCollection;
    private final Database mDatabase;

    private LiveData<List<Event>> mEventsLiveData;

    public EventSwipeViewModel(@NonNull Database database) {
        mDatabase = verifyNotNull(database);
        mEventCollection = database.query("events");
    }

    @NonNull
    public LiveData<List<Event>> getAll() {
        if(mEventsLiveData == null) {
            mEventsLiveData = mEventCollection.liveData(Event.class);
        }
        return mEventsLiveData;
    }
}
