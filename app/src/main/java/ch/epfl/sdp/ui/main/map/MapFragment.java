package ch.epfl.sdp.ui.main.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.firestore.FirebaseFirestore;
import ch.epfl.sdp.R;
import ch.epfl.sdp.databinding.FragmentMapBinding;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.map.MapManager;
import ch.epfl.sdp.platforms.firebase.db.FirestoreDatabase;
import ch.epfl.sdp.platforms.google.map.GoogleMapManager;
import ch.epfl.sdp.ui.main.swipe.EventDetailFragment;

public class MapFragment extends Fragment implements GoogleMap.OnMarkerClickListener {
    private MapViewModel mViewModel = null;
    private final MapViewModel.MapViewModelFactory mFactory;
    private FragmentMapBinding mBinding;
    private MapView mMapView;
    private  final static int PERMISSION_LOCATION=0;
    private boolean mLocationPermission = false;
    private Location mLastKnownLocation;
    private GoogleMapManager mGoogleMapManager = null;
    private float mZoomLevel = 12;

    public MapFragment() {
        mFactory = new MapViewModel.MapViewModelFactory();
        mFactory.setDatabase(new FirestoreDatabase(FirebaseFirestore.getInstance()));
    }

    @VisibleForTesting
    public MapFragment(@NonNull Database database, @NonNull MapManager mapManager) {
        mFactory = new MapViewModel.MapViewModelFactory();
        mFactory.setDatabase(database);
        mFactory.setMapManager(mapManager);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentMapBinding.inflate(inflater, container, false);
        mMapView = mBinding.getRoot().findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(googleMap -> {
            googleMap.setOnMarkerClickListener(this);
            mGoogleMapManager = new GoogleMapManager(googleMap);
        });

        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_LOCATION);

        return mBinding.getRoot();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (mGoogleMapManager != null) mFactory.setMapManager(mGoogleMapManager);
        mViewModel = new ViewModelProvider(this, mFactory).get(MapViewModel.class);

        mLocationPermission =
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (mLocationPermission) {
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(getContext().LOCATION_SERVICE);
            mLastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (mLastKnownLocation == null) {
                mLastKnownLocation = new Location("Europe");
                mLastKnownLocation.setLatitude(46.520564);
                mLastKnownLocation.setLongitude(6.567827);
                mZoomLevel = 4;
            }

            mViewModel.moveCamera(mLastKnownLocation, mZoomLevel);
            mViewModel.setMyLocation();
            mViewModel.addMarkers(getViewLifecycleOwner());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        EventDetailFragment infoFragment = new EventDetailFragment(mViewModel.getEventFromMarker(marker),this);
        this.getActivity().getSupportFragmentManager().beginTransaction().replace(this.getId(), infoFragment).commit();
        return true;
    }
}