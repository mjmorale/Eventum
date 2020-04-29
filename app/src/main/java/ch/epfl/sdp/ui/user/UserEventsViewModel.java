package ch.epfl.sdp.ui.user;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.ui.DatabaseViewModelFactory;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class UserEventsViewModel extends ViewModel {

    static class UserEventsViewModelFactory extends DatabaseViewModelFactory { }

    private final Database mDatabase;

    public UserEventsViewModel(@NonNull Database database) {
        mDatabase = verifyNotNull(database);
    }
}
