package ch.epfl.sdp.ui.main;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.RequiresApi;
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

import static android.app.PendingIntent.getActivity;
import static java.security.AccessController.getContext;

public class GoogleMapProvider implements MapProvider , OnMapReadyCallback {
    private static Boolean locationButtonEnabled;
    private static Boolean locationEnabled;
    private static GoogleMap map;
    private static Set<MarkerOptions> markerOptionsToBeAdded;
    private static final int PERMISSION_LOCATION=0;
    private static Context context;

    GoogleMapProvider(MapView mapView, Context context){
        mapView.getMapAsync(this);
        this.context = context;
        
    }


    @Override
    public void onMapReady(GoogleMap googlemap) {
        map = googlemap;
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity)context,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_LOCATION);
        } else {
            map.getUiSettings().setMyLocationButtonEnabled(locationEnabled);
            map.setMyLocationEnabled(true);
        }

//        for(MarkerOptions mo :markerOptionsToBeAdded)
//            map.addMarker(mo);
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
