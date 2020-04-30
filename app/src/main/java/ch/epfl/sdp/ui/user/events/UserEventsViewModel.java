package ch.epfl.sdp.ui.user.events;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import ch.epfl.sdp.Event;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.auth.UserInfo;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.FilterQuery;
import ch.epfl.sdp.ui.DatabaseViewModelFactory;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class UserEventsViewModel extends ViewModel {

    static class UserEventsViewModelFactory extends DatabaseViewModelFactory {

        public UserEventsViewModelFactory() {
            super(Authenticator.class);
        }

        public void setAuthenticator(@NonNull Authenticator authenticator) {
            setValue(0, verifyNotNull(authenticator));
        }
    }

    private LiveData<List<Event>> mOrganizedEvents;

    private FilterQuery mOrganizedEventsQuery;

    public UserEventsViewModel(@NonNull Authenticator authenticator, @NonNull Database database) {
        UserInfo user = authenticator.getCurrentUser();
        mOrganizedEventsQuery = database.query("events").whereFieldEqualTo("organizer", user.getUid());
    }

    public LiveData<List<Event>> getOrganizedEvents() {
        if(mOrganizedEvents == null) {
            mOrganizedEvents = mOrganizedEventsQuery.livedata(Event.class);
        }
        return mOrganizedEvents;
    }
}
