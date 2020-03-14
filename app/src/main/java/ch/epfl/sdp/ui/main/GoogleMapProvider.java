package ch.epfl.sdp.ui.main;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashSet;
import java.util.Set;

public class GoogleMapProvider implements MapProvider , OnMapReadyCallback {
    private Boolean locationButtonEnabled;
    private Boolean locationEnabled;
    private GoogleMap map;
    Set<MarkerOptions> markerOptionsToBeAdded;

    GoogleMapProvider(MapView mapView){
        mapView.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googlemap) {
        map = googlemap;
        map.getUiSettings().setMyLocationButtonEnabled(locationButtonEnabled);
        map.setMyLocationEnabled(locationEnabled);
        for(MarkerOptions mo :markerOptionsToBeAdded)
            map.addMarker(mo);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(46.520564, 6.567827), 9));
    }

    @Override
    public void setMyLocationButtonEnabled(Boolean enabled) {
        locationButtonEnabled= enabled;
    }

    @Override
    public void setMyLocationEnabled(Boolean enabled) {
        locationEnabled= enabled;
    }

    @Override
    public void addMarker(MarkerOptions markerOptions) {
        markerOptionsToBeAdded.add(markerOptions);
    }

    public void addMarker(Set<MarkerOptions> markerOptions) {
        for (MarkerOptions mo : markerOptions)
            addMarker(mo);
    }

}
