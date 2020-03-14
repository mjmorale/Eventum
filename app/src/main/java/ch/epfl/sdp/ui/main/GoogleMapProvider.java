package ch.epfl.sdp.ui.main;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashSet;
import java.util.Set;


public class GoogleMapProvider implements MapProvider , OnMapReadyCallback {
    public  Boolean locationButtonEnabled=false;
    public  Boolean locationEnabled=false;
    private  GoogleMap map;
    public  Set<MarkerOptions> markerOptionsToBeAdded= new HashSet<>();
    private  final int PERMISSION_LOCATION=0;
    private Context context;
    public boolean havePermission=false;
    MapView mapView;

    GoogleMapProvider(MapView mapView, Context context){
        this.mapView = mapView;
        this.context = context;

        if (!havePermission) {
            ActivityCompat.requestPermissions((Activity)context,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_LOCATION);
        }
        havePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED;
        
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googlemap) {
        map = googlemap;
        map.getUiSettings().setMyLocationButtonEnabled(locationButtonEnabled&&havePermission);
        map.setMyLocationEnabled(locationEnabled&&havePermission);
        map.addMarker(markerOptionsToBeAdded.iterator().next());
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
}
