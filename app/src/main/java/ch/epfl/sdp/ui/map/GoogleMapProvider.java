package ch.epfl.sdp.ui.map;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashSet;
import java.util.Set;


public class GoogleMapProvider implements MapProvider, OnMapReadyCallback {
    private  boolean mLocationButtonEnabled=false;
    private  boolean mLocationEnabled=false;
    private  GoogleMap mMap;
    private  Set<MarkerOptions> mMarkerOptionsToBeAdded= new HashSet<>();
    private  final int mPERMISSION_LOCATION=0;
    private Context mContext;
    private boolean mHavePermission=false;
    private MapView mMapView;

    GoogleMapProvider(Context context,MapView mapView){
        this.mMapView = mapView;
        this.mContext = context;

        if (!mHavePermission) {
            ActivityCompat.requestPermissions((Activity)context,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    mPERMISSION_LOCATION);
        }
        mHavePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED;

        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googlemap) {
        mMap = googlemap;
        mMap.getUiSettings().setMyLocationButtonEnabled(mLocationButtonEnabled&&mHavePermission);
        mMap.setMyLocationEnabled(mLocationEnabled&&mHavePermission);
        mMap.addMarker(mMarkerOptionsToBeAdded.iterator().next());
    }

    @Override
    public void setMyLocationButtonEnabled(Boolean enabled) {
        mLocationButtonEnabled= enabled;
    }

    @Override
    public void setMyLocationEnabled(Boolean enabled) {
        mLocationEnabled= enabled;
    }

    @Override
    public void addMarker(MarkerOptions markerOptions) {
        mMarkerOptionsToBeAdded.add(markerOptions);
    }
}
