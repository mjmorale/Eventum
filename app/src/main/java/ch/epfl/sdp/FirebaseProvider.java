package ch.epfl.sdp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;

import java.util.Arrays;
import java.util.List;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;


public class FirebaseProvider implements AuthProvider {
    private List<AuthUI.IdpConfig> providers= Arrays.asList(
            new AuthUI.IdpConfig.GoogleBuilder().build());

    @Override
    public Intent getLoginIntent() {
        return AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();
    }

    @Override
    public void logoutCurrentAccount(Context context, @Nullable OnCompleteListener<Void> onCompleteCallback) {
        AuthUI.getInstance().signOut(context).addOnCompleteListener(onCompleteCallback);
    }

    @Override
    public void deleteCurrentAccount(Context context, @Nullable OnCompleteListener<Void> onCompleteCallback) {
        AuthUI.getInstance().delete(context).addOnCompleteListener(onCompleteCallback);
    }

}
