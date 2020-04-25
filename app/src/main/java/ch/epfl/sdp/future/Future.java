package ch.epfl.sdp.future;

import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class Future<TType> {

    public interface FutureThen<TType> {
        void then(@NonNull TType data);
    }

    public interface FutureExcept {
        void except(@NonNull Exception e);
    }

    private final Task<TType> mTask;

    public Future(@NonNull Task<TType> task) {
        mTask = verifyNotNull(task);
    }

    public Future<TType> then(@NonNull FutureThen<TType> callback) {
        mTask.addOnSuccessListener(callback::then);

        return this;
    }

    public Future<TType> except(@NonNull FutureExcept callback) {
        mTask.addOnFailureListener(callback::except);

        return this;
    }
}
