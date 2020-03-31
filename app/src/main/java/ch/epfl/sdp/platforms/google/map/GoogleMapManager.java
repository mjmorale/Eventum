package ch.epfl.sdp.platforms.google.map;

import android.location.Location;

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

    public void moveCamera(Location location, float zoomLevel) {
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), zoomLevel));
    }
}
