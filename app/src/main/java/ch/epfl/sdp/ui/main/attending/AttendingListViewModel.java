package ch.epfl.sdp.ui.main.attending;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.DatabaseObject;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.ui.DatabaseViewModelFactory;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class AttendingListViewModel extends ViewModel {

    static class AttendingListViewModelFactory extends DatabaseViewModelFactory { }

    private final CollectionQuery mAttendingQuery;
    private final Database mDatabase;

    private LiveData<List<DatabaseObject<Event>>> mAttendingLiveData;

    public AttendingListViewModel(@NonNull Database database) {
        mDatabase = verifyNotNull(database);
        mAttendingQuery = database.query("events");
    }

    public LiveData<List<DatabaseObject<Event>>> getAttendingEvents() {
        if(mAttendingLiveData == null) {
            mAttendingLiveData = mAttendingQuery.liveData(Event.class);
        }
        return mAttendingLiveData;
    }
}