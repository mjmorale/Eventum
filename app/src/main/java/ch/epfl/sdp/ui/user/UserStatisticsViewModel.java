package ch.epfl.sdp.ui.user;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.ui.DatabaseViewModelFactory;
import ch.epfl.sdp.ui.ParameterizedViewModelFactory;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class UserStatisticsViewModel extends ViewModel {

    static class UserStatisticsViewModelFactory extends DatabaseViewModelFactory { }

    private final Database mDatabase;

    public UserStatisticsViewModel(@NonNull Database database) {
        mDatabase = verifyNotNull(database);
    }
}
