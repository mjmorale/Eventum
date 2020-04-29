package ch.epfl.sdp.map;

import android.location.Location;
import com.google.android.gms.maps.model.LatLng;

import androidx.annotation.NonNull;

public interface MapManager<TMarker> {

    TMarker addMarker(@NonNull String title, @NonNull LatLng location);

    void moveCamera(@NonNull Location location, float zoomLevel);

    /**
     * Method to move the camera (the focus) on the map
     *
     * @param location LatLng position on where to focus
     * @param zoomLevel the zoom on that location
     */
    void moveCamera(@NonNull LatLng location, float zoomLevel);

    /**
     * Clears all markers, shapes and polylines from the map
     */
    void clear();
}
