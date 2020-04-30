package ch.epfl.sdp.ui;

import androidx.annotation.NonNull;
import ch.epfl.sdp.db.Database;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class DatabaseViewModelFactory extends ParameterizedViewModelFactory {

    private int mDatabaseIndex;

    public DatabaseViewModelFactory(Class<?>... types) {
        super(types);
        mDatabaseIndex = types.length;

        addType(Database.class);
    }

    public void setDatabase(@NonNull Database database) {
        setValue(mDatabaseIndex, verifyNotNull(database));
    }
}
