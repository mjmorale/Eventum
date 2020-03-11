package ch.epfl.sdp.db.queries;

public class QueryResult<T> {

    private final boolean mSuccess;
    private final Exception mException;
    private final T mData;

    private QueryResult(T data, boolean success, Exception exception) {
        mSuccess = success;
        mException = exception;
        mData = data;
    }

    public static <T> QueryResult<T> success(T data) {
        return new QueryResult<T>(data, true, null);
    }

    public static <T> QueryResult<T> failure(Exception exception) {
        if(exception == null) {
            throw new IllegalArgumentException();
        }
        return new QueryResult<T>(null, false, exception);
    }

    public boolean isSuccessful() {
        return mSuccess;
    }

    public Exception getException() {
        return mException;
    }

    public T getData() {
        return mData;
    }
}
