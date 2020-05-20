package ch.epfl.sdp.platforms.android;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import ch.epfl.sdp.offline.ConnectivityService;

public class AndroidConnectivityService implements ConnectivityService {

    @Override
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
