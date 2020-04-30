package ch.epfl.sdp.db.queries;

import androidx.annotation.Nullable;


/**
 * Result object associated to a query.
 *
 * @param <T> type of the result object
 */
public class QueryResult<T> {

    private final boolean mSuccess;
    private final Exception mException;
    private final T mData;

    private QueryResult(@Nullable T data, boolean success, @Nullable Exception exception) {
        mSuccess = success;
        mException = exception;
        mData = data;
    }

    /**
     * Called when the query succeeded
     *
     * @param data returned object by the query
     * @param <T> type of the returned object
     * @return the associated QueryResult to this object
     */
    public static <T> QueryResult<T> success(@Nullable T data) {
        return new QueryResult<>(data, true, null);
    }

    /**
     * Called when the query failed
     *
     * @param exception returned in case of failure
     * @param <T> unused
     * @return the associated QueryResult to this object
     */
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
