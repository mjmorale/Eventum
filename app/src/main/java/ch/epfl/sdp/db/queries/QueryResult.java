package ch.epfl.sdp.db.queries;

import androidx.annotation.Nullable;

public class QueryResult<T> {

    private final boolean mSuccess;
    private final Exception mException;
    private final T mData;

    private QueryResult(@Nullable T data, boolean success, @Nullable Exception exception) {
        mSuccess = success;
        mException = exception;
        mData = data;
    }

    public static <T> QueryResult<T> success(@Nullable T data) {
        return new QueryResult<>(data, true, null);
    }

    public static <T> QueryResult<T> failure(@Nullable Exception exception) {
        return new QueryResult<>(null, false, exception);
    }

    public boolean isSuccessful() {
        return mSuccess;
    }

    @Nullable
    public Exception getException() {
        return mException;
    }

    @Nullable
    public T getData() {
        return mData;
    }
}
