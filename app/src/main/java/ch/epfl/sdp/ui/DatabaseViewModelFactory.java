package ch.epfl.sdp.ui;

import androidx.annotation.NonNull;
import ch.epfl.sdp.db.Database;

public class DatabaseViewModelFactory extends ParameterizedViewModelFactory {

    private int mDatabaseIndex;

    public DatabaseViewModelFactory(Class<?>... types) {
        super(types);
        mDatabaseIndex = types.length;

        addType(Database.class);
    }

    public void setDatabase(@NonNull Database database) {
        setValue(mDatabaseIndex, database);
    }
}
