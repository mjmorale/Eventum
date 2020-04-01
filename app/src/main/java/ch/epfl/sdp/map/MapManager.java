package ch.epfl.sdp.map;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public interface MapManager {
    void addMarker(MarkerOptions markerOptions);
    void addMarker(String title, LatLng location);
    void moveCamera(Location location, float zoomLevel);
    void setMyLocation();
}
