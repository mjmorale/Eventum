package ch.epfl.sdp.offline;

import android.content.Context;

/**
 * Service that answers queries about the state of network connectivity.
 */
public interface ConnectivityService {

    /**
     * Returns details about the currently active default data network.
     *
     * @param context current application context
     * @return true if a network connection is available, false otherwise
     */
    boolean isNetworkAvailable(Context context);
}
