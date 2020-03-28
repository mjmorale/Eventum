package ch.epfl.sdp.ui.user;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.ui.ParameterizedViewModelFactory;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class UserStatisticsViewModel extends ViewModel {

    static class UserStatisticsViewModelFactory extends ParameterizedViewModelFactory {

        UserStatisticsViewModelFactory() {
            super(Database.class);
        }

        void setDatabase(@NonNull Database database) {
            setValue(0, verifyNotNull(database));
        }
    }

    private final Database mDatabase;

    public UserStatisticsViewModel(@NonNull Database database) {
        mDatabase = verifyNotNull(database);
    }
}
