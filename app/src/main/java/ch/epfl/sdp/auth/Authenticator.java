package ch.epfl.sdp.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import ch.epfl.sdp.future.Future;

/**
 * Interface that represents an authentication service for users in the application.
 *
 * @param <T> The type of credential that the authentication services uses.
 */
public interface Authenticator<T> {

    /**
     * Try to login using an email address and a password.
     *
     * @param email The user's email address.
     * @param password The user's password.
     * @throws IllegalArgumentException One or more argument is null.
     *
     * @return The future result of the authentication request.
     */
    Future<UserInfo> login(@NonNull String email, @NonNull String password);

    /**
     * Try to login using a credential object.
     *
     * @param credential The credential object.
     * @throws IllegalArgumentException One or more argument is null.
     *
     * @return The future result of the authentication request.
     */
    Future<UserInfo> login(@NonNull T credential);

    /**
     * Logout the current user. If no user is logged in then this is a harmless no-op.
     */
    void logout();

    /**
     * @return The current logged in user information or null if no user is logged in.
     */
    @Nullable
    UserInfo getCurrentUserInfo();
}
