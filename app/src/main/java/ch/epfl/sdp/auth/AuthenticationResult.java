package ch.epfl.sdp.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

/**
 * Wrapper around an authentication result. Either contains a successful request with the
 * queried data, or an exception that describes the error.
 * @see Authenticator
 */
public class AuthenticationResult {

    private final Exception mException;
    private final boolean mIsSuccess;
    private final UserInfo mUserInfo;

    private AuthenticationResult(@Nullable UserInfo user, boolean success, @Nullable Exception exception) {
        this.mUserInfo = user;
        this.mIsSuccess = success;
        this.mException = exception;
    }

    /**
     * Construct a new AuthenticationResult that contains the successfully queried data.
     *
     * @param userInfo The queried authentication data
     * @throws IllegalArgumentException The user info is null
     *
     * @return A new instance of AuthenticationResult
     */
    public static AuthenticationResult success(@NonNull UserInfo userInfo) {
        return new AuthenticationResult(verifyNotNull(userInfo), true, null);
    }

    /**
     * Construct a new AuthenticationResult that contains the error that occurred during the
     * authentication operation.
     *
     * @param exception The error that occurred during the authentication operation
     * @throws IllegalArgumentException The exception is null
     *
     * @return A new instance of AuthenticationResult
     */
    public static AuthenticationResult failure(@NonNull Exception exception) {
        return new AuthenticationResult(null, false, verifyNotNull(exception));
    }

    /**
     * @return True if the operation was successful, false if an error occurred.
     */
    public boolean isSuccessful() {
        return mIsSuccess;
    }

    /**
     * @return The authentication data or null if an error occurred.
     * @see #isSuccessful()
     */
    public UserInfo getUserInfo() {
        return mUserInfo;
    }

    /**
     * @return The exception that occurred or null if the operation was successful
     * @see #isSuccessful()
     */
    public Exception getException() {
        return mException;
    }
}
