package ch.epfl.sdp.ui.map;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class GoogleMapProvider implements MapProvider, OnMapReadyCallback {
    private  boolean mLocationButtonEnabled=false;
    private  boolean mLocationEnabled=false;
    private  GoogleMap mMap;
    private  Set<MarkerOptions> mMarkerOptionsToBeAdded= new HashSet<>();
    private  final static int PERMISSION_LOCATION=0;
    private Context mContext;
    private boolean mHavePermission=false;
    private MapView mMapView;
    private Activity mActivity;
    private Location mCurrentLocation;
    private float mZoomLevel;

    GoogleMapProvider(Context context, MapView mapView, Activity activity){
        this.mMapView = mapView;
        this.mContext = context;
        this.mActivity = activity;

        if (!mHavePermission) {
            ActivityCompat.requestPermissions((Activity)context,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_LOCATION);
        }
        mHavePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED;
        mapView.getMapAsync(this);

        if (!mHavePermission) {
            mCurrentLocation.setLatitude(46.520564);
            mCurrentLocation.setLongitude(6.567827);
            mZoomLevel = 3;
        } else {
            LocationManager locationManager = (LocationManager) mActivity.getSystemService(mContext.LOCATION_SERVICE);
            mCurrentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            mZoomLevel = 12;
        }
    }

    @Override
    public void onMapReady(GoogleMap googlemap) {
        mMap = googlemap;
        mMap.getUiSettings().setMyLocationButtonEnabled(mLocationButtonEnabled&&mHavePermission);
        mMap.setMyLocationEnabled(mLocationEnabled&&mHavePermission);

        Iterator<MarkerOptions> mIterator =mMarkerOptionsToBeAdded.iterator();
        while (mIterator!=null && mIterator.hasNext()){
            MarkerOptions toBeAdded = mIterator.next();
            mMap.addMarker(toBeAdded);
            mIterator.remove();
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), mZoomLevel));
    }

    @Override
    public void setMyLocationButtonEnabled(boolean enabled) {
        mLocationButtonEnabled= enabled;
    }

    @Override
    public void setMyLocationEnabled(boolean enabled) {
        mLocationEnabled= enabled;
    }

    @Override
    public void addMarker(MarkerOptions markerOptions) {
        mMarkerOptionsToBeAdded.add(markerOptions);
    }
}