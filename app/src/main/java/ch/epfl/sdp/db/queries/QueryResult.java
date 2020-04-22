package ch.epfl.sdp.db.queries;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class QueryResult<T> {

    private final boolean mSuccess;
    private final Exception mException;
    private final T mData;
    private final String mReference;

    private QueryResult(@Nullable T data, boolean success, @Nullable Exception exception) {
        this(data, success, null, exception);
    }

    private QueryResult(@Nullable T data, boolean success, @Nullable String docReference, @Nullable Exception exception) {
        mSuccess = success;
        mException = exception;
        mData = data;
        mReference = docReference;
    }

    public static <T> QueryResult<T> success(@Nullable T data, @Nullable String id) {
        return new QueryResult<>(data, true, id,null);
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

    @Nullable
    public String getReference(){return mReference;}
}
