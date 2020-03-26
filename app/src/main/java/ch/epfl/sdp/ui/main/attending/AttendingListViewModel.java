package ch.epfl.sdp.ui.main.attending;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import ch.epfl.sdp.Event;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.ui.DatabaseViewModel;

public class AttendingListViewModel extends DatabaseViewModel {

    private CollectionQuery mAttendingQuery;
    private LiveData<List<Event>> mAttendingLiveData;

    public AttendingListViewModel(@NonNull Database database) {
        super(database);
        mAttendingQuery = mDatabase.query("events");
    }

    public LiveData<List<Event>> getAttendingEvents() {
        if(mAttendingLiveData == null) {
            mAttendingLiveData = mAttendingQuery.liveData(Event.class);
        }
        return mAttendingLiveData;
    }
}
