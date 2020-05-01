package ch.epfl.sdp.ui.main.attending;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.ui.DatabaseViewModelFactory;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

/**
 * View model used for the list of events a user attends to
 */
public class AttendingListViewModel extends ViewModel {

    /**
     * Factory for the AttendingListViewModel
     */
    static class AttendingListViewModelFactory extends DatabaseViewModelFactory { }

    private final CollectionQuery mAttendingQuery;
    private final Database mDatabase;

    private LiveData<List<Event>> mAttendingLiveData;

    /**
     * Constructor of the AttendingListViewModel, the factory should be used instead of this
     *
     * @param database where the events are stored
     */
    public AttendingListViewModel(@NonNull Database database) {
        mDatabase = verifyNotNull(database);
        mAttendingQuery = database.query("events");
    }

    /**
     * Method to get the list of events a user attends to
     *
     * @return a live list of events
     */
    public LiveData<List<Event>> getAttendingEvents() {
        if(mAttendingLiveData == null) {
            mAttendingLiveData = mAttendingQuery.liveData(Event.class);
        }
        return mAttendingLiveData;
    }
}