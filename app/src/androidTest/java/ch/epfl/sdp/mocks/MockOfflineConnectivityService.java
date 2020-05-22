package ch.epfl.sdp.mocks;

import android.content.Context;

import ch.epfl.sdp.offline.ConnectivityService;

public class MockOfflineConnectivityService implements ConnectivityService {
    @Override
    public boolean isNetworkAvailable(Context context) {
        return false;
    }
}
