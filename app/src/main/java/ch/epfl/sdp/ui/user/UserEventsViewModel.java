package ch.epfl.sdp.ui.user;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.ui.ParameterizedViewModelFactory;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class UserEventsViewModel extends ViewModel {

    static class UserEventsViewModelFactory extends ParameterizedViewModelFactory {

        UserEventsViewModelFactory() {
            super(Database.class);
        }

        void setDatabase(@NonNull Database database) {
            setValue(0, verifyNotNull(database));
        }

        //TODO: @Dorian remove once this viewmodel is fully implemented
        void test() {}
    }

    private final Database mDatabase;

    public UserEventsViewModel(@NonNull Database database) {
        mDatabase = verifyNotNull(database);
    }
}
