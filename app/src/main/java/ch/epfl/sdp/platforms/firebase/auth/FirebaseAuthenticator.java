package ch.epfl.sdp.platforms.firebase.auth;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import ch.epfl.sdp.auth.AuthenticationResult;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.User;
import ch.epfl.sdp.auth.UserInfo;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class FirebaseAuthenticator implements Authenticator<AuthCredential> {

    private final FirebaseAuth mAuth;

    public FirebaseAuthenticator(@NonNull FirebaseAuth firebaseAuth) {
        this.mAuth = verifyNotNull(firebaseAuth);
    }

    @Override
    public void login(@NonNull String email, @NonNull String password, @NonNull final OnLoginCallback callback) {
        verifyNotNull(email, password, callback);

        Task<AuthResult> authResultTask = mAuth.createUserWithEmailAndPassword(email, password);
        handleAuthResultTask(authResultTask, callback);
    }

    @Override
    public void login(@NonNull AuthCredential credential, @NonNull final OnLoginCallback callback) {
        verifyNotNull(credential, callback);

        Task<AuthResult> authResultTask = mAuth.signInWithCredential(credential);
        handleAuthResultTask(authResultTask, callback);
    }

    @Override
    public void logout() {
        mAuth.signOut();
    }

    @Override
    @Nullable
    public UserInfo getCurrentUser() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if(firebaseUser == null) {
            return null;
        }
        return new UserInfo(firebaseUser.getUid(), firebaseUser.getDisplayName(), firebaseUser.getEmail());
    }

    private void handleAuthResultTask(@NonNull Task<AuthResult> authResultTask, @NonNull OnLoginCallback callback) {
        verifyNotNull(authResultTask, callback);

        authResultTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                UserInfo userInfo = getCurrentUser();
                callback.onLoginComplete(AuthenticationResult.success(userInfo));
            } else {
                callback.onLoginComplete(AuthenticationResult.failure(task.getException()));
            }
        });
    }
}
