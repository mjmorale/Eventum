package ch.epfl.sdp.ui;

import androidx.annotation.NonNull;
import ch.epfl.sdp.db.Database;

/**
 * Factory to create a view model with a database
 */
public class DatabaseViewModelFactory extends ParameterizedViewModelFactory {

    private int mDatabaseIndex;

    /**
     * Constructor of the DatabaseViewModelFactory
     *
     * @param types of attributes added to the view model
     */
    public DatabaseViewModelFactory(Class<?>... types) {
        super(types);
        mDatabaseIndex = types.length;

        addType(Database.class);
    }

    public void setDatabase(@NonNull Database database) {
        setValue(mDatabaseIndex, database);
    }
}
