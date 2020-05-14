package ch.epfl.sdp.ui.main.map;

import android.location.LocationManager;
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
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Marker;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.R;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.databinding.FragmentMapBinding;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.DatabaseObject;
import ch.epfl.sdp.map.LocationService;
import ch.epfl.sdp.map.MapManager;
import ch.epfl.sdp.platforms.google.map.GoogleLocationService;
import ch.epfl.sdp.platforms.google.map.GoogleMapManager;
import ch.epfl.sdp.ui.main.FilterSettingsViewModel;
import ch.epfl.sdp.ui.main.swipe.SwipeFragment;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

/**
 * Fragment for the map with events markers
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {
    private MapViewModel mViewModel;
    private final MapViewModel.MapViewModelFactory mFactoryMap;
    private FilterSettingsViewModel.FilterSettingsViewModelFactory mFactoryFilterSettings;
    private FragmentMapBinding mBinding;
    private LocationService mLocationService = null;

    private MapView mMapView;
    private float mZoomLevel = 12;

    /**
     * Constructor of the map fragment, only for testing purpose!
     *
     * @param mapManager {@link ch.epfl.sdp.map.MapManager}
     * @param locationService {@link ch.epfl.sdp.map.LocationService}
     * @param database {@link ch.epfl.sdp.db.Database}
     */
    @VisibleForTesting
    public MapFragment(@NonNull MapManager mapManager, @NonNull LocationService locationService, @NonNull Database database, @NonNull Authenticator authenticator) {
        verifyNotNull(mapManager, database, locationService);
        mFactoryMap = new MapViewModel.MapViewModelFactory();
        mFactoryMap.setMapManager(mapManager);
        mLocationService = locationService;
        mFactoryFilterSettings = new FilterSettingsViewModel.FilterSettingsViewModelFactory();
        mFactoryFilterSettings.setDatabase(database);
        mFactoryFilterSettings.setLocationService(locationService);
        mFactoryFilterSettings.setAuthenticator(authenticator);
    }

    /**
     * Constructor of the map fragment (initialize the map view model)
     */
    public MapFragment() {
        mFactoryMap = new MapViewModel.MapViewModelFactory();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentMapBinding.inflate(inflater, container, false);
        mMapView = mBinding.getRoot().findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        if (mLocationService == null) {
            mLocationService =
                    new GoogleLocationService((LocationManager) getActivity().getSystemService(getContext().LOCATION_SERVICE));

        }
        mFactoryMap.setLocationService(mLocationService);

        mMapView.getMapAsync(this);

        return mBinding.getRoot();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        googleMap.setMyLocationEnabled(true);

        mFactoryMap.setMapManager(new GoogleMapManager(googleMap));
        mViewModel = new ViewModelProvider(this, mFactoryMap).get(MapViewModel.class);

        FilterSettingsViewModel filterSettingsViewModel =
                new ViewModelProvider(requireActivity(), mFactoryFilterSettings).get(FilterSettingsViewModel.class);

        filterSettingsViewModel.getFilteredEvents().observe(getViewLifecycleOwner(), events -> {
            mViewModel.clearEvents();
            for(DatabaseObject<Event> event: events)
                mViewModel.addEvent(event.getObject());
        });

        mViewModel.centerCamera(getContext(), mZoomLevel);

        googleMap.setInfoWindowAdapter(new MapMarkerInfoWindowView(mViewModel,getContext()));

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Bundle bundle = new Bundle();
                bundle.putInt("eventHash", mViewModel.getEventFromMarker(marker).hashCode());
                SwipeFragment swipeFragment = new SwipeFragment();
                swipeFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.mapView, swipeFragment).commit();
            }
        });
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
}