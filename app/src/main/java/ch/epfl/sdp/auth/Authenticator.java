package ch.epfl.sdp.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Interface that represents an authentication service for users in the application.
 *
 * @param <T> The type of credential that the authentication services uses.
 */
public interface Authenticator<T> {

    /**
     * Callback interface for authentication operations.
     */
    interface OnLoginCallback {

        /**
         * Called after a login operation is complete.
         *
         * @param result The login operation result.
         * @see AuthenticationResult
         */
        void onLoginComplete(@NonNull AuthenticationResult result);
    }

    /**
     * Try to login using an email address and a password.
     *
     * @param email The user's email address.
     * @param password The user's password.
     * @param callback The authentication operation callback.
     * @throws IllegalArgumentException One or more argument is null.
     */
    void login(@NonNull String email, @NonNull String password, @NonNull final OnLoginCallback callback);

    /**
     * Try to login using a credential object.
     *
     * @param credential The credential object.
     * @param callback The authentication operation callback.
     * @throws IllegalArgumentException One or more argument is null.
     */
    void login(@NonNull T credential, @NonNull final OnLoginCallback callback);

    /**
     * Logout the current user. If no user is logged in then this is a harmless no-op.
     */
    void logout();

    /**
     * @return The current logged in user information or null if no user is logged in.
     * @see UserInfo
     */
    @Nullable
    UserInfo getCurrentUser();
}
