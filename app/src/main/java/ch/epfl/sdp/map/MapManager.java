package ch.epfl.sdp.map;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public interface MapManager {
    Marker addMarker(MarkerOptions markerOptions);
    Marker addMarker(String title, LatLng location);
    void moveCamera(Location location, float zoomLevel);
    void setMyLocation();
}
