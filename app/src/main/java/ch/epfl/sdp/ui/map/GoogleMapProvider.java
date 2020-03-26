package ch.epfl.sdp.ui.map;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import java.util.List;

public class GoogleMapProvider implements MapProvider, OnMapReadyCallback {
    private  boolean mLocationEnabled=false;
    private List<MarkerOptions> mMarkerOptions;
    private  final static int PERMISSION_LOCATION=0;
    private Context mContext;
    private boolean mHavePermission;
    private Activity mActivity;
    private LatLng mCurrentLatLng;
    private float mZoomLevel;
    private GoogleMap mMap = null;

    public GoogleMapProvider(Fragment fragment, MapView mapView){
        this.mContext = fragment.getContext();
        this.mActivity = fragment.getActivity();
        mMarkerOptions = new ArrayList<>();

        // default current location
        mCurrentLatLng = new LatLng(46.520564, 6.567827);
        mZoomLevel = 4;

        ActivityCompat.requestPermissions((Activity)mContext,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_LOCATION);

        mHavePermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED;

        if (mHavePermission){
            LocationManager locationManager = (LocationManager) mActivity.getSystemService(mContext.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            mCurrentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            mZoomLevel = 12;
        }
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setMapSettings(mMap, mMarkerOptions, mLocationEnabled&&mHavePermission);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mCurrentLatLng, mZoomLevel));
    }

    static public void setMapSettings(GoogleMap googleMap, List<MarkerOptions> markerOptionsList, boolean locationEnabled) {
        googleMap.setMyLocationEnabled(locationEnabled);
        for(MarkerOptions markerOptions: markerOptionsList)googleMap.addMarker(markerOptions);
    }

    @Override
    public void setMyLocationEnabled(boolean enabled) {
        mLocationEnabled= enabled;
    }

    @Override
    public void addMarker(MarkerOptions markerOptions) {
        if(mMap==null){
            mMarkerOptions.add(markerOptions);
        }else{
            mMap.addMarker(markerOptions);
        }
    }
}