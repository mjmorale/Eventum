package ch.epfl.sdp.map;

import android.content.Context;
import android.location.Location;

/**
 * Location service for giving the last known location of the android device
 */
public interface LocationService {
    /**
     * Method to get the last known location
     *
     * @param context the environment the application is currently running in
     * @return the last know location of the android device
     */
    Location getLastKnownLocation(Context context);

    /**
     * Method to get the current state of location
     *
     * @return the current enabled/disabled state of location
     */
    boolean isLocationEnabled();
}