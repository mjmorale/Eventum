package ch.epfl.sdp.ui.main.attending;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import ch.epfl.sdp.Event;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.ui.ParameterizedViewModelFactory;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class AttendingListViewModel extends ViewModel {

    static class AttendingListViewModelFactory extends ParameterizedViewModelFactory {

        AttendingListViewModelFactory() {
            super(Database.class);
        }

        void setDatabase(@NonNull Database database) {
            setValue(0, verifyNotNull(database));
        }
    }

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
