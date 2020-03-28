package ch.epfl.sdp.ui.main.attending;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import ch.epfl.sdp.Event;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class AttendingListViewModel extends ViewModel {

    private final CollectionQuery mAttendingQuery;
    private final Database mDatabase;

    private LiveData<List<Event>> mAttendingLiveData;

    public AttendingListViewModel(@NonNull Database database) {
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
