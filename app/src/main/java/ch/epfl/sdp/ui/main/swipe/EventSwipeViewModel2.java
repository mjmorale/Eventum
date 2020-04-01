package ch.epfl.sdp.ui.main.swipe;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.ui.ParameterizedViewModelFactory;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class EventSwipeViewModel2 extends ViewModel {

    static class EventSwipeViewModelFactory2 extends ParameterizedViewModelFactory {

        EventSwipeViewModelFactory2() {
            super(Database.class);
        }

        void setDatabase(@NonNull Database database) {
            setValue(0, verifyNotNull(database));
        }
    }

    private final CollectionQuery mAttendingQuery;
    private final Database mDatabase;

    private LiveData<List<Event>> mAttendingLiveData;

    public EventSwipeViewModel2(@NonNull Database database) {
        mDatabase = verifyNotNull(database);
        mAttendingQuery = database.query("events");
    }

    public LiveData<List<Event>> getAttendingEvents() {
        if(mAttendingLiveData == null) {
            mAttendingLiveData = mAttendingQuery.liveData(Event.class);
        }
        return mAttendingLiveData;
    }
}
