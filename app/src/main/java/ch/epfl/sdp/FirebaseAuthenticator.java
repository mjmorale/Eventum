package ch.epfl.sdp;

import android.content.Context;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import ch.epfl.sdp.AuthenticationResult;
import ch.epfl.sdp.Authenticator;
import ch.epfl.sdp.User;

public class FirebaseAuthenticator implements Authenticator {

    private final FirebaseAuth mAuth;

    public FirebaseAuthenticator(FirebaseAuth auth) {
        mAuth = auth;
    }

    @Override
    public void login(@NonNull String email, @NonNull String password, @Nullable final OnLoginCallback callback) {
        if(email == null || password == null) {
            throw new IllegalArgumentException();
        }

        Task<AuthResult> authResultTask = mAuth.createUserWithEmailAndPassword(email, password);
        if(callback != null) {
            authResultTask.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        User user = new User(firebaseUser.getUid(), firebaseUser.getDisplayName(), firebaseUser.getEmail());
                        callback.onLoginComplete(AuthenticationResult.success(user));
                    } else {
                        callback.onLoginComplete(AuthenticationResult.failure(task.getException()));
                    }
                }
            });
        }
    }

    @Override
    public void logout() {
        mAuth.signOut();
    }

    public void login(@NonNull AuthCredential credential, @Nullable final OnLoginCallback callback) {
        if(credential == null) {
            throw new IllegalArgumentException();
        }

        Task<AuthResult> authResultTask = mAuth.signInWithCredential(credential);
        if(callback != null) {
            authResultTask.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        User user = new User(firebaseUser.getUid(), firebaseUser.getDisplayName(), firebaseUser.getEmail());
                        callback.onLoginComplete(AuthenticationResult.success(user));
                    } else {
                        callback.onLoginComplete(AuthenticationResult.failure(task.getException()));
                    }
                }
            });
        }
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
}
