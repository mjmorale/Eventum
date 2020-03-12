package ch.epfl.sdp.auth.firebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import ch.epfl.sdp.auth.AuthenticationResult;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.auth.User;

public class FirebaseAuthenticator implements Authenticator<AuthCredential> {

    private final FirebaseAuth mAuth;

    public FirebaseAuthenticator(@NonNull FirebaseAuth firebaseAuth) {
        if(firebaseAuth == null) {
            throw new IllegalArgumentException();
        }
        this.mAuth = firebaseAuth;
    }

    @Override
    public void login(@NonNull String email, @NonNull String password, @Nullable final OnLoginCallback callback) {
        if(email == null || password == null) {
            throw new IllegalArgumentException();
        }

        Task<AuthResult> authResultTask = mAuth.createUserWithEmailAndPassword(email, password);
        handleAuthResultTask(authResultTask, callback);
    }

    @Override
    public void login(@NonNull AuthCredential credential, @Nullable final OnLoginCallback callback) {
        if(credential == null) {
            throw new IllegalArgumentException();
        }

        Task<AuthResult> authResultTask = mAuth.signInWithCredential(credential);
        handleAuthResultTask(authResultTask, callback);
    }

    @Override
    public void logout() {
        mAuth.signOut();
    }

    @Override
    public User getCurrentUser() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if(firebaseUser == null) {
            return null;
        }
        User user = new User(firebaseUser.getUid(), firebaseUser.getDisplayName(), firebaseUser.getEmail());
        return user;
    }

    private void handleAuthResultTask(Task<AuthResult> authResultTask, OnLoginCallback callback) {
        if(callback != null) {
            authResultTask.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    User user = new User(firebaseUser.getUid(), firebaseUser.getDisplayName(), firebaseUser.getEmail());
                    callback.onLoginComplete(AuthenticationResult.success(user));
                } else {
                    callback.onLoginComplete(AuthenticationResult.failure(task.getException()));
                }
            });
        }
    }
}
