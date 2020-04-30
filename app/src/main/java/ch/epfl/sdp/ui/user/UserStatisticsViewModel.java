package ch.epfl.sdp.ui.user;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.ui.DatabaseViewModelFactory;
import ch.epfl.sdp.ui.ParameterizedViewModelFactory;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

/**
 * View model to display statistics about a user
 */
public class UserStatisticsViewModel extends ViewModel {

    /**
     * Factory for the UserStatisticsViewModel
     */
    static class UserStatisticsViewModelFactory extends DatabaseViewModelFactory { }

    private final Database mDatabase;

    /**
     * Constructor of the UserStatisticsViewModel, the factory should be used instead of this
     *
     * @param database {@link ch.epfl.sdp.db.Database}
     */
    public UserStatisticsViewModel(@NonNull Database database) {
        mDatabase = verifyNotNull(database);
    }
}
