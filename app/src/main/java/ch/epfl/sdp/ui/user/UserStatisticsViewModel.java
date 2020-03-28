package ch.epfl.sdp.ui.user;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import ch.epfl.sdp.db.Database;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class UserStatisticsViewModel extends ViewModel {

    private final Database mDatabase;

    public UserStatisticsViewModel(@NonNull Database database) {
        mDatabase = verifyNotNull(database);
    }
}
