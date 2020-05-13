package ch.epfl.sdp.map;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

/**
 * Location service to get the last known location of the android device
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
     * Returns the approximate distance in meters between the current
     * location and the given location. Distance is defined using
     * the WGS84 ellipsoid.
     *
     * @param context environment the application is currently running in
     * @param location the destination location
     * @return the approximate distance in meters
     */
    float distanceTo(Context context, LatLng location);
}