package ch.epfl.sdp.platforms.firebase.auth;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.auth.UserInfo;
import ch.epfl.sdp.future.Future;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

/**
 * Authenticator service for Firebase.
 */
public class FirebaseAuthenticator implements Authenticator<AuthCredential> {

    private final FirebaseAuth mAuth;

    /**
     * Construct a new FirebaseAuthenticator.
     *
     * @param firebaseAuth A reference to the firebase authentication API.
     * @throws IllegalArgumentException The firebase authentication object is null.
     */
    public FirebaseAuthenticator(@NonNull FirebaseAuth firebaseAuth) {
        this.mAuth = verifyNotNull(firebaseAuth);
    }

    @Override
    public Future<UserInfo> login(@NonNull String email, @NonNull String password) {
        verifyNotNull(email, password);

        Task<AuthResult> authResultTask = mAuth.createUserWithEmailAndPassword(email, password);
        return handleAuthResultTask(authResultTask);
    }

    @Override
    public Future<UserInfo> login(@NonNull AuthCredential credential) {
        verifyNotNull(credential);

        Task<AuthResult> authResultTask = mAuth.signInWithCredential(credential);
        return handleAuthResultTask(authResultTask);
    }

    @Override
    public void logout() {
        mAuth.signOut();
    }

    @Nullable
    @Override
    public UserInfo getCurrentUserInfo() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if(firebaseUser == null) {
            return null;
        }
        return new UserInfo(firebaseUser.getUid(), firebaseUser.getDisplayName(), firebaseUser.getEmail());
    }

    private Future<UserInfo> handleAuthResultTask(Task<AuthResult> authResultTask) {
        return new Future<>(authResultTask.continueWith(task -> {
            FirebaseUser user = task.getResult().getUser();
            if(user != null) {
                return new UserInfo(user.getUid(), user.getDisplayName(), user.getEmail());
            }
            else {
                throw new NullPointerException("No user connected");
            }
        }));
    }
}
