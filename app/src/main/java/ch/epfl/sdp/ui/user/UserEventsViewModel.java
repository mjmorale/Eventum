package ch.epfl.sdp.ui.user;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.ui.DatabaseViewModelFactory;
import ch.epfl.sdp.ui.ParameterizedViewModelFactory;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

/**
 * View model for the user's events
 */
public class UserEventsViewModel extends ViewModel {

    /**
     * Factory for the UserEventsViewModel
     */
    static class UserEventsViewModelFactory extends DatabaseViewModelFactory { }

    private final Database mDatabase;

    /**
     * Constructor of the UserEventsViewModel, the factory should be used instead of this
     *
     * @param database {@link ch.epfl.sdp.db.Database}
     */
    public UserEventsViewModel(@NonNull Database database) {
        mDatabase = verifyNotNull(database);
    }
}
