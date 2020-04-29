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

public class AttendingListViewModel extends ViewModel {

    static class AttendingListViewModelFactory extends DatabaseViewModelFactory {

        public AttendingListViewModelFactory() {
            super(Authenticator.class);
        }

        public void setAuthenticator(@NonNull Authenticator authenticator) {
            setValue(0, authenticator);
        }
    }

    private final FilterQuery mAttendingQuery;
    private final String mUserRef;

    private LiveData<List<DatabaseObject<Event>>> mAttendingLiveData;

    public AttendingListViewModel(@NonNull Authenticator authenticator, @NonNull Database database) {
        UserInfo user = authenticator.getCurrentUser();
        mUserRef = user.getUid();
        mAttendingQuery = database.query("events").whereArrayContains("attendees", user.getUid());
    }

    public LiveData<List<DatabaseObject<Event>>> getAttendingEvents() {
        if(mAttendingLiveData == null) {
            mAttendingLiveData = mAttendingQuery.liveData(Event.class);
        }
        return mAttendingLiveData;
    }
}