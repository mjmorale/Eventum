package ch.epfl.sdp.platforms.google.map;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ch.epfl.sdp.map.MapManager;

public class GoogleMapManager implements MapManager {
    private GoogleMap mGoogleMap;

    public GoogleMapManager(GoogleMap googleMap){
        mGoogleMap= googleMap;
    }

    @Override
    public void addMarker(MarkerOptions markerOptions) {
        mGoogleMap.addMarker(markerOptions);
    }

    @Override
    public void addMarker(String title, LatLng location) {
        addMarker(new MarkerOptions().position(location).title(title));
    }

    @Override
    public void moveCamera(LatLng location, float zoomLevel) {
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel));
    }

    @Override
    public void setMyLocationEnabled() {
        mGoogleMap.setMyLocationEnabled(true);
    }
}
