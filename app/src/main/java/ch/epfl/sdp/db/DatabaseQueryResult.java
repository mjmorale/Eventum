package ch.epfl.sdp.db;

import java.util.List;
import java.util.Map;

public class DatabaseQueryResult<T> {

    private final List<T> mData;
    private final boolean mSuccess;
    private final Exception mException;

    private DatabaseQueryResult(List<T> data, boolean success, Exception exception) {
        this.mData = data;
        this.mSuccess = success;
        this.mException = exception;
    }

    public static <T> DatabaseQueryResult success(List<T> data) {
        for(T object: data) {
            if(object == null) {
                throw new IllegalArgumentException();
            }
        }
        return new DatabaseQueryResult<T>(data, true, null);
    }

    public static <T> DatabaseQueryResult failure(Exception exception) {
        if(exception == null) {
            throw new IllegalArgumentException();
        }
        return new DatabaseQueryResult<T>(null, false, exception);
    }

    public boolean isSuccessful() {
        return mSuccess;
    }

    public List<T> getData() {
        return mData;
    }

    public Exception getException() {
        return mException;
    }
}
