package ch.epfl.sdp.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import ch.epfl.sdp.db.Database;

public class DatabaseViewModel extends ViewModel {

    protected final Database mDatabase;

    public DatabaseViewModel(@NonNull Database database) {
        if(database == null) {
            throw new IllegalArgumentException();
        }
        mDatabase = database;
    }
}
