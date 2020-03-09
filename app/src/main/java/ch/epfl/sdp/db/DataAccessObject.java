package ch.epfl.sdp.db;

import androidx.annotation.NonNull;

public abstract class DataAccessObject<T> {

    protected final Database mDb;

    protected DataAccessObject(@NonNull Database database) {
        if(database == null) {
            throw new IllegalArgumentException();
        }
        mDb = database;
    }
}
