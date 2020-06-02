package ch.epfl.sdp.ui.main.swipe;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.Objects;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.R;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.databinding.FragmentSwipeBinding;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.DatabaseObject;
import ch.epfl.sdp.map.LocationService;
import ch.epfl.sdp.platforms.google.map.GoogleLocationService;
import ch.epfl.sdp.platforms.google.map.GoogleMapManager;
import ch.epfl.sdp.ui.event.LiteMapViewModel;
import ch.epfl.sdp.ui.main.FilterSettingsViewModel;
import ch.epfl.sdp.ui.main.MainActivity;
import ch.epfl.sdp.ui.main.map.MapFragment;

/**
 * Fragment for the swiping cards
 */
public class SwipeFragment extends Fragment implements SwipeFlingAdapterView.onFlingListener, OnMapReadyCallback {

    private FragmentSwipeBinding mBinding;
    private ArrayAdapter<DatabaseObject<Event>> mArrayAdapter;
    private LocationService mLocationService;
    private int mNumberSwipe = 0;
    private float mZoomLevel = 15;

    private FilterSettingsViewModel.FilterSettingsViewModelFactory mSettingsFactory;
    private FilterSettingsViewModel mSettingsViewModel;

    private final LiteMapViewModel.LiteMapViewModelFactory mMapFactory;
    private LiteMapViewModel mMapViewModel;

    private final int DEFAULT_VALUE = 0;
    private int mEventHash;

    public SwipeFragment() {
        mMapFactory = new LiteMapViewModel.LiteMapViewModelFactory();
    }

    /**
     * Constructor of the swipe fragment, only for testing purpose!
     *
     * @param database {@link ch.epfl.sdp.db.Database}
     */
    @VisibleForTesting
    public SwipeFragment(@NonNull Database database, @NonNull Authenticator authenticator, @NonNull LocationService locationService) {
        mSettingsFactory = new FilterSettingsViewModel.FilterSettingsViewModelFactory();
        mSettingsFactory.setDatabase(database);
        mSettingsFactory.setAuthenticator(authenticator);
        mSettingsFactory.setLocationService(locationService);
        mLocationService = locationService;

        mMapFactory = new LiteMapViewModel.LiteMapViewModelFactory();
    }

    @Override
    public void removeFirstObjectInAdapter() {
        mArrayAdapter.remove(mArrayAdapter.getItem(0));
    }

    @Override
    public void onLeftCardExit(Object o) {
        mNumberSwipe += 1;
        if (mEventHash == DEFAULT_VALUE) mBinding.swipeEmptyMsg.setVisibility(mArrayAdapter.isEmpty() ? View.VISIBLE : View.INVISIBLE);
        goBackToMapIfConditionsAreMet();
    }

    @Override
    public void onRightCardExit(Object o) {
        mSettingsViewModel.joinEvent(((DatabaseObject<Event>) o).getId(), result -> {
            if(!result.isSuccessful()) {
                Toast.makeText(getContext(), "Cannot accept event.", Toast.LENGTH_SHORT).show();
            }
        });
        mNumberSwipe += 1;
        if (mEventHash == DEFAULT_VALUE) mBinding.swipeEmptyMsg.setVisibility(mArrayAdapter.isEmpty() ? View.VISIBLE : View.INVISIBLE);
        goBackToMapIfConditionsAreMet();
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

        if (mLocationService == null)
            mLocationService = new GoogleLocationService((LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE));

        mArrayAdapter = new CardArrayAdapter(getContext(), mLocationService);
        mArrayAdapter.setNotifyOnChange(true);

        mBinding.cardsListView.setAdapter(mArrayAdapter);
        mBinding.cardsListView.setFlingListener(this);

        mBinding.eventDetailView.getMapView().onCreate(savedInstanceState);
        mBinding.eventDetailView.getMapView().getMapAsync(this);

        mSettingsViewModel = new ViewModelProvider(requireActivity(), mSettingsFactory).get(FilterSettingsViewModel.class);

        mEventHash = bundleEventHash();

        addEventsInArrayAdapter();

        mBinding.eventDetailView.setOnClickListener(click -> showEventDetail());

        return mBinding.getRoot();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMapViewModel = new ViewModelProvider(this, mMapFactory).get(LiteMapViewModel.class);

        mBinding.cardsListView.setOnItemClickListener((itemPosition, dataObject) -> {
            Event selectedEvent = mArrayAdapter.getItem(itemPosition).getObject();
            mMapViewModel.setEventOnMap(new GoogleMapManager(googleMap), selectedEvent.getLocation(), selectedEvent.getTitle(), mZoomLevel);
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

        if (getActivity() instanceof MainActivity) //for test don't need to update toolbar
            ((MainActivity) requireActivity()).updateToolBarSwipe(true);
    }

    private void showCardList() {
        mBinding.cardsListView.setVisibility(View.VISIBLE);
        mBinding.eventDetailView.setVisibility(View.GONE);

        if (getActivity() instanceof MainActivity)  //for test don't need to update toolbar
            ((MainActivity) getActivity()).updateToolBarSwipe(false);
    }

    private void setupBackButton() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (mEventHash != DEFAULT_VALUE && mBinding.eventDetailView.getVisibility() == View.GONE) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.cards_list_view_container, new MapFragment()).commit();
                } else {
                    showCardList();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    private void addEventsInArrayAdapter() {
        mSettingsViewModel.getFilteredEvents().observe(getViewLifecycleOwner(), events -> {
            if (events != null) {
                mBinding.swipeEmptyMsg.setVisibility(events.isEmpty() ? View.VISIBLE : View.INVISIBLE);

                if (mEventHash != DEFAULT_VALUE) {
                    for (DatabaseObject<Event> event : events) {
                        if (mEventHash == event.getObject().hashCode()) {
                            mArrayAdapter.clear();
                            mArrayAdapter.add(event);
                        }
                    }
                } else {
                    mArrayAdapter.clear();
                    mArrayAdapter.addAll(events);
                    mArrayAdapter.sort((o1, o2) -> o1.getObject().getDate().compareTo(o2.getObject().getDate()));
                }
                mNumberSwipe = 0;
            }
        });
    }

    private int bundleEventHash() {
        Bundle bundle = this.getArguments();
        int eventHash = DEFAULT_VALUE;
        if (bundle != null) {
            eventHash = bundle.getInt("eventHash", DEFAULT_VALUE);
        }
        return eventHash;
    }

    private void goBackToMapIfConditionsAreMet() {
        if (mArrayAdapter.isEmpty() && mEventHash != DEFAULT_VALUE) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.cards_list_view_container, new MapFragment()).commit();
        }
    }
}
