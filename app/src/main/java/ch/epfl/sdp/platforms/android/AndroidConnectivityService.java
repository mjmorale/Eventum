package ch.epfl.sdp.platforms.android;

import ch.epfl.sdp.offline.ConnectivityService;

public class AndroidConnectivityService implements ConnectivityService {

    private boolean mIsConnected;

    public AndroidConnectivityService(boolean isConnected) {
        mIsConnected = isConnected;
    }

    @Override
    public boolean isNetworkAvailable() {
        return mIsConnected;
    }

}
