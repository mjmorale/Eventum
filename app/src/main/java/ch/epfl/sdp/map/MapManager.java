package ch.epfl.sdp.map;

import android.location.Location;
import com.google.android.gms.maps.model.LatLng;
import androidx.annotation.NonNull;

/**
 * Map manager to handle a map
 *
 * @param <TMarker> markers (points of interest) added on the map
 */
public interface MapManager<TMarker> {

    /**
     * Method to add a marker on the map
     *
     * @param title name of the marker
     * @param location position of the marker
     * @return the marker that has been added to the map
     */
    TMarker addMarker(@NonNull String title, @NonNull LatLng location);

    /**
     * Method to move the camera (the focus) on the map
     *
     * @param location position where to focus
     * @param zoomLevel the zoom on that location
     */
    void moveCamera(@NonNull Location location, float zoomLevel);
}
