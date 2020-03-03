package ch.epfl.sdp;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;

public interface AuthProvider {

    Intent getLoginIntent();

    void logoutCurrentAccount(Context context, @Nullable OnCompleteListener<Void> onCompleteCallback);
    void deleteCurrentAccount(Context context, @Nullable OnCompleteListener<Void> onCompleteCallback);
}
