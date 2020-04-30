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

/**
 * View model for the UserEventsFragment.
 * This view model provides the fragment with a list of events organized by the current user.
 */
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

    /**
     * Construct a new UserEventsViewModel. Use the factory method instead of this constructor
     *
     * @param authenticator The authentication service to use
     * @param database The database service to use
     */
    public UserEventsViewModel(@NonNull Authenticator authenticator, @NonNull Database database) {
        UserInfo user = authenticator.getCurrentUser();
        mOrganizedEventsQuery = database.query("events").whereFieldEqualTo("organizer", user.getUid());
    }

    /**
     * @return A livedata containing the list of events organized by the current user.
     * @see LiveData
     */
    public LiveData<List<Event>> getOrganizedEvents() {
        if(mOrganizedEvents == null) {
            mOrganizedEvents = mOrganizedEventsQuery.livedata(Event.class);
        }
        return mOrganizedEvents;
    }
}
