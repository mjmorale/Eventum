package ch.epfl.sdp.ui.main.swipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.R;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.databinding.FragmentSwipeBinding;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.DatabaseObject;
import ch.epfl.sdp.map.LocationService;
import ch.epfl.sdp.platforms.google.map.GoogleMapManager;
import ch.epfl.sdp.ui.event.LiteMapViewModel;
import ch.epfl.sdp.ui.main.FilterSettingsViewModel;

/**
 * Fragment for the swiping cards
 */
public class SwipeFragment extends Fragment implements SwipeFlingAdapterView.onFlingListener, OnMapReadyCallback {

    private FragmentSwipeBinding mBinding;
    private ArrayAdapter<DatabaseObject<Event>> mArrayAdapter;
    private int mNumberSwipe = 0;
    private float mZoomLevel = 15;

    private FilterSettingsViewModel.FilterSettingsViewModelFactory mSettingsFactory;
    private FilterSettingsViewModel mSettingsViewModel;

    private final LiteMapViewModel.LiteMapViewModelFactory mMapFactory;
    private LiteMapViewModel mMapViewModel;

    private final int DEFAULT_VALUE = 0;

    public SwipeFragment() {
        mMapFactory = new LiteMapViewModel.LiteMapViewModelFactory();
    }

    /**
     * Constructor of the swipe fragment, only for testing purpose!
     *
     * @param database {@link ch.epfl.sdp.db.Database}
     */
    @VisibleForTesting
    public SwipeFragment(@NonNull Database database, @NonNull Authenticator authenticator, LocationService locationService) {
        mSettingsFactory = new FilterSettingsViewModel.FilterSettingsViewModelFactory();
        mSettingsFactory.setDatabase(database);
        mSettingsFactory.setAuthenticator(authenticator);
        mSettingsFactory.setLocationService(locationService);

        mMapFactory = new LiteMapViewModel.LiteMapViewModelFactory();
    }

    @Override
    public void removeFirstObjectInAdapter() {
        mArrayAdapter.remove(mArrayAdapter.getItem(0));
    }

    @Override
    public void onLeftCardExit(Object o) {
        mNumberSwipe += 1;
    }

    @Override
    public void onRightCardExit(Object o) {
        mSettingsViewModel.joinEvent(((DatabaseObject<Event>) o).getId(), result -> {
            if(!result.isSuccessful()) {
                Toast.makeText(getContext(), "Cannot accept event.", Toast.LENGTH_SHORT).show();
            }
        });
        mNumberSwipe += 1;
    }

    @Override
    public void onAdapterAboutToEmpty(int i) {}

    @Override
    public void onScroll(float scrollProgressPercent) {
        SwipeFlingAdapterView flingContainer = mBinding.cardsListView;
        flingContainer.getSelectedView().findViewById(R.id.deny_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
        flingContainer.getSelectedView().findViewById(R.id.accept_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentSwipeBinding.inflate(inflater, container, false);

        mArrayAdapter = new CardArrayAdapter(getContext());
        mArrayAdapter.setNotifyOnChange(true);

        mBinding.cardsListView.setAdapter(mArrayAdapter);
        mBinding.cardsListView.setFlingListener(this);

        mBinding.eventDetailView.getMapView().onCreate(savedInstanceState);
        mBinding.eventDetailView.getMapView().getMapAsync(this);

        mSettingsViewModel = new ViewModelProvider(requireActivity(), mSettingsFactory).get(FilterSettingsViewModel.class);


        mSettingsViewModel.getFilteredEvents().observe(getViewLifecycleOwner(), events -> {

           if (events != null) {
                mBinding.swipeEmptyMsg.setVisibility(events.isEmpty() ? View.VISIBLE : View.INVISIBLE);
                mArrayAdapter.clear();
                mArrayAdapter.addAll(events);
                mArrayAdapter.sort((o1, o2) -> o1.getObject().getDate().compareTo(o2.getObject().getDate()));
                mNumberSwipe = 0;
               specificEventFromBundleOnTop();
            }
        });

        mBinding.eventDetailView.setOnClickListener(click -> showEventDetail());

        return mBinding.getRoot();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMapFactory.setMapManager(new GoogleMapManager(googleMap));
        mMapViewModel = new ViewModelProvider(this, mMapFactory).get(LiteMapViewModel.class);

        mBinding.cardsListView.setOnItemClickListener((itemPosition, dataObject) -> {
            Event selectedEvent = mArrayAdapter.getItem(itemPosition).getObject();
            mMapViewModel.setEventOnMap(selectedEvent.getLocation(), selectedEvent.getTitle(), mZoomLevel);
            mBinding.eventDetailView.setEvent(selectedEvent);
            mBinding.eventDetailView.callOnClick();
        });

        setupBackButton();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    private void showEventDetail() {
        mBinding.cardsListView.setVisibility(View.GONE);
        mBinding.eventDetailView.setVisibility(View.VISIBLE);
    }

    private void showCardList() {
        mBinding.eventDetailView.setVisibility(View.GONE);
        mBinding.cardsListView.setVisibility(View.VISIBLE);
    }

    private void setupBackButton() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showCardList();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    private void specificEventFromBundleOnTop() {
        // to have a event on top when clicking on it on the map
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            float eventHash = bundle.getFloat("eventHash", DEFAULT_VALUE);
            if (eventHash != DEFAULT_VALUE) {
                DatabaseObject<Event> searchedDatabaseObject = null;
                for (int index = 0; index < mArrayAdapter.getCount() && searchedDatabaseObject == null; index++) {
                    DatabaseObject<Event> databaseObject = mArrayAdapter.getItem(index);
                    if (eventHash == (databaseObject.getObject().hashCode())) {
                        searchedDatabaseObject = databaseObject;
                    }
                }
                if (searchedDatabaseObject != null) {
                    mArrayAdapter.remove(searchedDatabaseObject); // not working !!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    mArrayAdapter.insert(searchedDatabaseObject, 0);
                }
            }
        }
    }
}
