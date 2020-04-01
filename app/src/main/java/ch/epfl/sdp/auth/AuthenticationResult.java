package ch.epfl.sdp.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import ch.epfl.sdp.User;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class AuthenticationResult {

    private final Exception mException;
    private final boolean mIsSuccess;
    private final User mUser;

    private AuthenticationResult(@Nullable User user, boolean success, @Nullable Exception exception) {
        this.mUser = user;
        this.mIsSuccess = success;
        this.mException = exception;
    }

    public static AuthenticationResult success(@NonNull User user) {
        return new AuthenticationResult(verifyNotNull(user), true, null);
    }

    public static AuthenticationResult failure(@NonNull Exception exception) {
        return new AuthenticationResult(null, false, verifyNotNull(exception));
    }

    public boolean isSuccessful() {
        return mIsSuccess;
    }

    public User getUser() {
        return mUser;
    }

    public Exception getException() {
        return mException;
    }
}
