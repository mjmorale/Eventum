package ch.epfl.sdp.ui.user;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.ui.DatabaseViewModelFactory;

public class UserEventsViewModel extends ViewModel {

    static class UserEventsViewModelFactory extends DatabaseViewModelFactory { }

    public UserEventsViewModel(@NonNull Database database) {

    }
}
