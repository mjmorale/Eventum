package ch.epfl.sdp.platforms.google.map;

import android.location.Location;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import androidx.annotation.NonNull;
import ch.epfl.sdp.map.MapManager;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class GoogleMapManager implements MapManager<Marker> {

    private final GoogleMap mGoogleMap;

    public GoogleMapManager(@NonNull GoogleMap googleMap){
        verifyNotNull(googleMap);
        mGoogleMap = googleMap;
    }

    @Override
    public Marker addMarker(@NonNull String title, @NonNull LatLng location) {
        verifyNotNull(title);
        verifyNotNull(location);
        MarkerOptions markerOptions = new MarkerOptions().position(location).title(title);
        return mGoogleMap.addMarker(markerOptions);
    }



    @Override
    public void moveCamera(@NonNull Location location, float zoomLevel) {
        verifyNotNull(location);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), zoomLevel));
    }

    @Override
    public void moveCamera(@NonNull LatLng location, float zoomLevel) {
        verifyNotNull(location);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel));
    }

    @Override
    public void clear() {
        mGoogleMap.clear();

    }
}
