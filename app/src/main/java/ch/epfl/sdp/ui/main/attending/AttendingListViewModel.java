package ch.epfl.sdp.ui.main.attending;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.auth.UserInfo;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.DatabaseObject;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.FilterQuery;
import ch.epfl.sdp.ui.DatabaseViewModelFactory;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

/**
 * View model used for the list of events a user attends to
 */
public class AttendingListViewModel extends ViewModel {

    /**
     * Factory for the AttendingListViewModel
     */
    static class AttendingListViewModelFactory extends DatabaseViewModelFactory {

        public AttendingListViewModelFactory() {
            super(Authenticator.class);
        }

        public void setAuthenticator(@NonNull Authenticator authenticator) {
            setValue(0, authenticator);
        }
    }

    private final FilterQuery mAttendingQuery;

    private LiveData<List<DatabaseObject<Event>>> mAttendingLiveData;

    /**
     * Constructor of the AttendingListViewModel, the factory should be used instead of this
     *
     * @param authenticator The authentication service to use
     * @param database The database service to use
     */
    public AttendingListViewModel(@NonNull Authenticator authenticator, @NonNull Database database) {
        UserInfo user = authenticator.getCurrentUser();
        mAttendingQuery = database.query("events").whereArrayContains("attendees", user.getUid());
    }

    /**
     * Method to get the list of events a user attends to
     *
     * @return a live list of events
     */
    public LiveData<List<DatabaseObject<Event>>> getAttendingEvents() {
        if(mAttendingLiveData == null) {
            mAttendingLiveData = mAttendingQuery.liveData(Event.class);
        }
        return mAttendingLiveData;
    }
}