package ch.epfl.sdp.map;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public interface MapManager {
    void addMarker(MarkerOptions markerOptions);
    void addMarker(String title, LatLng location);
    void moveCamera(LatLng location, float zoomLevel);
    void setMyLocationEnabled();
}
