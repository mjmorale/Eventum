package ch.epfl.sdp;

import android.content.Intent;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;

public interface AuthProvider {

    Intent getLoginIntent();

    void logoutCurrentAccount(@Nullable OnCompleteListener<Void> onCompleteCallback);
    void deleteCurrentAccount(@Nullable OnCompleteListener<Void> onCompleteCallback);
}
