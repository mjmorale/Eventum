package ch.epfl.sdp.ui.user;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import ch.epfl.sdp.db.Database;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class UserEventsViewModel extends ViewModel {

    private final Database mDatabase;

    public UserEventsViewModel(@NonNull Database database) {
        mDatabase = verifyNotNull(database);
    }
}
