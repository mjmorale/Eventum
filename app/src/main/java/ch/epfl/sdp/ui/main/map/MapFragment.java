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

import java.io.File;

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
import ch.epfl.sdp.storage.Storage;
import ch.epfl.sdp.ui.ImageViewModel;
import ch.epfl.sdp.ui.ServiceProvider;
import ch.epfl.sdp.ui.main.FilterSettingsViewModel;
import ch.epfl.sdp.ui.main.swipe.SwipeFragment;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

/**
 * Fragment for the map with events markers
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private MapViewModel mMapViewModel;
    private final MapViewModel.MapViewModelFactory mMapViewModelFactory;

    private FilterSettingsViewModel.FilterSettingsViewModelFactory mFilterSettingsViewModelFactory;

    private ImageViewModel mImageViewModel;
    private final ImageViewModel.ImageViewModelFactory mImageViewModelFactory;

    private FragmentMapBinding mBinding;

    private MapView mMapView;
    private float mZoomLevel = 12;

    /**
     * Constructor of the map fragment, only for testing purposes!
     *
     * @param mapManager {@link MapManager}
     * @param locationService {@link LocationService}
     * @param database {@link Database}
     * @param authenticator {@link Authenticator}
     * @param storage {@link Storage}
     * @param cacheDir The image cache directory.
     */
    @VisibleForTesting
    public MapFragment(@NonNull MapManager mapManager,
                       @NonNull LocationService locationService,
                       @NonNull Database database,
                       @NonNull Authenticator authenticator,
                       @NonNull Storage storage,
                       @NonNull File cacheDir) {
        verifyNotNull(mapManager, database, locationService, authenticator, storage, cacheDir);

        mMapViewModelFactory = new MapViewModel.MapViewModelFactory();
        mMapViewModelFactory.setMapManager(mapManager);

        mImageViewModelFactory = new ImageViewModel.ImageViewModelFactory();
        mImageViewModelFactory.setStorage(storage);
        mImageViewModelFactory.setCacheDir(cacheDir);

        mFilterSettingsViewModelFactory = new FilterSettingsViewModel.FilterSettingsViewModelFactory();
        mFilterSettingsViewModelFactory.setDatabase(database);
        mFilterSettingsViewModelFactory.setLocationService(locationService);
        mFilterSettingsViewModelFactory.setAuthenticator(authenticator);
    }

    /**
     * Construct a new MapFragment
     */
    public MapFragment() {
        mImageViewModelFactory = new ImageViewModel.ImageViewModelFactory();
        mImageViewModelFactory.setStorage(ServiceProvider.getInstance().getStorage());

        mMapViewModelFactory = new MapViewModel.MapViewModelFactory();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentMapBinding.inflate(inflater, container, false);
        mMapView = mBinding.getRoot().findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        if (mMapViewModelFactory.getLocationService() == null) {
            mMapViewModelFactory.setLocationService(
                new GoogleLocationService((LocationManager) getActivity().getSystemService(getContext().LOCATION_SERVICE))
            );
        }

        if(mImageViewModelFactory.getCacheDir() == null) {
            mImageViewModelFactory.setCacheDir(getContext().getCacheDir());
        }
        mImageViewModel = new ViewModelProvider(this, mImageViewModelFactory).get(ImageViewModel.class);

        mMapView.getMapAsync(this);

        return mBinding.getRoot();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMyLocationEnabled(true);

        mMapViewModelFactory.setMapManager(new GoogleMapManager(googleMap));
        mMapViewModel = new ViewModelProvider(this, mMapViewModelFactory).get(MapViewModel.class);

        FilterSettingsViewModel filterSettingsViewModel =
                new ViewModelProvider(requireActivity(), mFilterSettingsViewModelFactory).get(FilterSettingsViewModel.class);

        filterSettingsViewModel.getFilteredEvents().observe(getViewLifecycleOwner(), events -> {
            mMapViewModel.clearEvents();
            for(DatabaseObject<Event> event: events)
                mMapViewModel.addEvent(event.getObject());
        });

        mMapViewModel.centerCamera(getContext(), mZoomLevel);

        googleMap.setInfoWindowAdapter(new MapMarkerInfoWindowView(getContext(), mMapViewModel, mImageViewModel));

        googleMap.setOnInfoWindowClickListener(marker -> {
            Bundle bundle = new Bundle();
            bundle.putInt("eventHash", mMapViewModel.getEventFromMarker(marker).hashCode());
            SwipeFragment swipeFragment = new SwipeFragment();
            swipeFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mapView, swipeFragment).commit();
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