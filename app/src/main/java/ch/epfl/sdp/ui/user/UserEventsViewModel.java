package ch.epfl.sdp.ui.user;

import androidx.annotation.NonNull;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.ui.DatabaseViewModel;

public class UserEventsViewModel extends DatabaseViewModel {

    public UserEventsViewModel(@NonNull Database database) {
        super(database);
    }
}
