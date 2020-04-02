package ch.epfl.sdp.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import ch.epfl.sdp.User;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class AuthenticationResult {

    private final Exception mException;
    private final boolean mIsSuccess;
    private final UserInfo mUserInfo;

    private AuthenticationResult(@Nullable UserInfo user, boolean success, @Nullable Exception exception) {
        this.mUserInfo = user;
        this.mIsSuccess = success;
        this.mException = exception;
    }

    public static AuthenticationResult success(@NonNull UserInfo userInfo) {
        return new AuthenticationResult(verifyNotNull(userInfo), true, null);
    }

    public static AuthenticationResult failure(@NonNull Exception exception) {
        return new AuthenticationResult(null, false, verifyNotNull(exception));
    }

    public boolean isSuccessful() {
        return mIsSuccess;
    }

    public UserInfo getUserInfo() {
        return mUserInfo;
    }

    public Exception getException() {
        return mException;
    }
}
