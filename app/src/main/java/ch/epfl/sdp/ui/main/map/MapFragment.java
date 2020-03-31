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

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.FirebaseFirestore;

import ch.epfl.sdp.R;
import ch.epfl.sdp.databinding.FragmentMapBinding;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.map.MapManager;
import ch.epfl.sdp.platforms.firebase.db.FirestoreDatabase;
import ch.epfl.sdp.platforms.google.map.GoogleMapManager;

public class MapFragment extends Fragment {

    private MapViewModel mViewModel=null;
    private final MapViewModel.MapViewModelFactory mFactory;
    private FragmentMapBinding mBinding;
    private MapView mMapView;

    private  final static int PERMISSION_LOCATION=0;
    private boolean mLocationPermission = false;
    private LatLng mLastKnowLocation;


    public MapFragment() {
        mFactory = new MapViewModel.MapViewModelFactory();
        mFactory.setDatabase(new FirestoreDatabase(FirebaseFirestore.getInstance()));
    }

    @VisibleForTesting
    public MapFragment(Database database, MapManager mapManager) {
        mFactory = new MapViewModel.MapViewModelFactory();
        mFactory.setDatabase(database);
        mFactory.setMapManager(mapManager);
        mViewModel = new ViewModelProvider(this, mFactory).get(MapViewModel.class);
    }

    @VisibleForTesting
    public MapFragment(MapViewModel mapViewModel){
        mFactory = new MapViewModel.MapViewModelFactory();
        mViewModel = mapViewModel;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
<<<<<<< HEAD


=======
>>>>>>> b9adddfd3309de70cd3cc8b973c7fdbbca630ebd
        mBinding = FragmentMapBinding.inflate(inflater, container, false);
        mMapView = mBinding.getRoot().findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(googleMap -> {
            if(mViewModel==null){
                mFactory.setMapManager(new GoogleMapManager(googleMap));
                mViewModel = new ViewModelProvider(this, mFactory).get(MapViewModel.class);
            }
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_LOCATION);
        });

        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_LOCATION);

        return mBinding.getRoot();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){

        mLocationPermission =
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (mLocationPermission) {
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(getContext().LOCATION_SERVICE);

<<<<<<< HEAD
            Location mylocation =locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            mLastKnowLocation= new LatLng(mylocation.getLatitude(), mylocation.getLongitude());

            if (mLastKnowLocation != null){
                mViewModel.moveCamera(mLastKnowLocation, 12);
                mViewModel.setMyLocationEnabled();
            }
=======
            if (mLastKnowLocation != null) {
                mViewModel.moveCameraOnMapManager(mLastKnowLocation, 12);
                mViewModel.setLocationOnMapManager();
            }
        } else {
            mViewModel.moveCameraOnMapManagerDefaultLocation();
>>>>>>> b9adddfd3309de70cd3cc8b973c7fdbbca630ebd
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
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
}