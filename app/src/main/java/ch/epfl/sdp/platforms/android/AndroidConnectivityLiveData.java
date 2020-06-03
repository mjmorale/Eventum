package ch.epfl.sdp.platforms.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.lifecycle.LiveData;

public class AndroidConnectivityLiveData extends LiveData<Boolean> {

    private Context mContext;
    private Boolean currentConnectivityState = false;
    private BroadcastReceiver mNetworkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Boolean newConnectivityState = isConnected(context);
            // This avoids reloading if connection changed between mobile data and wifi
            if (currentConnectivityState != newConnectivityState) {
                currentConnectivityState = newConnectivityState;
                postValue(currentConnectivityState);
            }
        }
    };

    public AndroidConnectivityLiveData(Context context) {
        mContext = context;
    }

    @Override
    protected void onActive() {
        super.onActive();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        mContext.registerReceiver(mNetworkReceiver, filter);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        mContext.unregisterReceiver(mNetworkReceiver);
    }

    /**
     * Synchronously query connectivity, useful if we don't want to wait on a listener.
     *
     * @param context current context
     * @return if connection is available
     */
    public Boolean isConnected(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
