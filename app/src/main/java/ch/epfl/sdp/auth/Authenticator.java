package ch.epfl.sdp.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface Authenticator<T> {

    interface OnLoginCallback {
        void onLoginComplete(AuthenticationResult result);
    }

    void login(@NonNull String email, @NonNull String password, @Nullable final OnLoginCallback callback);

    void login(@NonNull T credential, @Nullable final OnLoginCallback callback);

    void logout();

    User getCurrentUser();
}
