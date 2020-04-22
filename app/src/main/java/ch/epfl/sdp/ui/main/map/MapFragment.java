package ch.epfl.sdp.ui.main.map;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.Marker;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.R;
import ch.epfl.sdp.databinding.FragmentMapBinding;
import ch.epfl.sdp.map.LocationService;
import ch.epfl.sdp.map.MapManager;
import ch.epfl.sdp.platforms.google.map.GoogleLocationService;
import ch.epfl.sdp.platforms.google.map.GoogleMapManager;
import ch.epfl.sdp.ui.main.FilterSettingsViewModel;
import ch.epfl.sdp.ui.main.swipe.EventDetailFragment;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class MapFragment extends Fragment implements GoogleMap.OnMarkerClickListener {

    private MapViewModel mViewModel;
    private final MapViewModel.MapViewModelFactory mFactory;
    private FragmentMapBinding mBinding;

    private MapView mMapView;
    private LocationService mLocationService;
    private Location mLastKnownLocation;
    private float mZoomLevel = 12;

    public MapFragment() {
        mFactory = new MapViewModel.MapViewModelFactory();
        mLocationService = GoogleLocationService.getInstance();
    }

    @VisibleForTesting
    public MapFragment(@NonNull MapManager mapManager, @NonNull LocationService locationService) {
        verifyNotNull(mapManager);
        mFactory = new MapViewModel.MapViewModelFactory();
        mFactory.setMapManager(mapManager);
        mLocationService = locationService;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentMapBinding.inflate(inflater, container, false);
        mMapView = mBinding.getRoot().findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mLastKnownLocation = mLocationService.getLastKnownLocation(getContext());
        if (mLastKnownLocation == null) {
            mLastKnownLocation = new Location("Europe");
            mLastKnownLocation.setLatitude(46.520564);
            mLastKnownLocation.setLongitude(6.567827);
            mZoomLevel = 4;
        }

        mMapView.getMapAsync(googleMap -> {
            googleMap.setOnMarkerClickListener(this);
            googleMap.setMyLocationEnabled(true);

            mFactory.setMapManager(new GoogleMapManager(googleMap));
            mViewModel = new ViewModelProvider(this, mFactory).get(MapViewModel.class);

            FilterSettingsViewModel filterSettingsViewModel =
                    new ViewModelProvider(requireActivity()).get(FilterSettingsViewModel.class);

            filterSettingsViewModel.getFilteredEvents().observe(getViewLifecycleOwner(), events -> {
                mViewModel.clearEvents();
                for(Event event: events)
                    mViewModel.addEvent(event);
            });

            mViewModel.moveCamera(mLastKnownLocation, mZoomLevel);
        });

        return mBinding.getRoot();
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
        getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(this.getId(), new EventDetailFragment(mViewModel.getEventFromMarker(marker),this))
                .commit();
        return true;
    }
}