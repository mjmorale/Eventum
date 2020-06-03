package ch.epfl.sdp.offline;

import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleService;

import ch.epfl.sdp.platforms.android.AndroidConnectivityLiveData;
import ch.epfl.sdp.ui.auth.AuthActivity;

public class ConnectivityService extends LifecycleService {

    private AndroidConnectivityLiveData mConnectivityLiveData =
            new AndroidConnectivityLiveData(this);

    @Override
    public void onCreate() {
        super.onCreate();
        mConnectivityLiveData.observe(this,
                isConnected -> startActivity(new Intent(this, AuthActivity.class)));
    }

    @Nullable
    @Override
    public IBinder onBind(@NonNull Intent intent) {
        super.onBind(intent);
        return null;
    }
}
