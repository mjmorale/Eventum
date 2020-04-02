package ch.epfl.sdp.platforms.google.map;

import android.location.Location;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import ch.epfl.sdp.map.MapManager;

public class GoogleMapManager implements MapManager {
    private GoogleMap mGoogleMap;

    public GoogleMapManager(GoogleMap googleMap){
        mGoogleMap = googleMap;
    }

    @Override
    public Marker addMarker(MarkerOptions markerOptions) {
        return mGoogleMap.addMarker(markerOptions);
    }

    @Override
    public Marker addMarker(String title, LatLng location) {
        return addMarker(new MarkerOptions().position(location).title(title));
    }

    @Override
    public void moveCamera(Location location, float zoomLevel) {
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), zoomLevel));
    }

    @Override
    public void setMyLocation() {
        mGoogleMap.setMyLocationEnabled(true);
    }
}
