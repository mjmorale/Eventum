package ch.epfl.sdp.ui.main.swipe;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.R;
import ch.epfl.sdp.databinding.FragmentSwipeBinding;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.map.LocationService;
import ch.epfl.sdp.platforms.firebase.db.FirestoreDatabase;
import ch.epfl.sdp.platforms.google.map.GoogleLocationService;

public class SwipeFragment extends Fragment implements SwipeFlingAdapterView.onFlingListener {

    private final EventSwipeViewModel.EventSwipeViewModelFactory mFactory;
    private FragmentSwipeBinding mBinding;
    private EventSwipeViewModel mViewModel;
    private ArrayAdapter<Event> mArrayAdapter;
    private List<Event> mEventList;
    private int mNumberSwipe = 0;

    private EventDetailFragment mInfoFragment;
    private LocationService mLocationService;

    @Override
    public void removeFirstObjectInAdapter() {
        mEventList.remove(0);
        mArrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLeftCardExit(Object o) {
        mNumberSwipe += 1;
    }

    @Override
    public void onRightCardExit(Object o) {
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

    public SwipeFragment() {
        mFactory = new EventSwipeViewModel.EventSwipeViewModelFactory();
        mFactory.setDatabase(new FirestoreDatabase(FirebaseFirestore.getInstance()));
        mLocationService = GoogleLocationService.getInstance();
    }

    @VisibleForTesting
    public SwipeFragment(@NonNull Database database, @NonNull LocationService locationService) {
        mFactory = new EventSwipeViewModel.EventSwipeViewModelFactory();
        mFactory.setDatabase(database);
        mLocationService = locationService;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentSwipeBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @SuppressLint("CheckResult")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mEventList = new ArrayList<>();
        mArrayAdapter = new CardArrayAdapter(getContext(), mEventList);
        mBinding.cardsListView.setAdapter(mArrayAdapter);
        mBinding.cardsListView.setFlingListener(this);

        mViewModel = new ViewModelProvider(this, mFactory).get(EventSwipeViewModel.class);
        if(mViewModel.getSwipeLiveData() != null && mViewModel.getSwipeLiveData().hasObservers()) {
            mViewModel.getSwipeLiveData().removeObservers(getViewLifecycleOwner());
        }

        View rootView = mBinding.getRoot().getRootView();
        SeekBar seekBarRange = rootView.findViewById(R.id.seekBar_range);


        seekBarRange.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Location location = mLocationService.getLastKnownLocation(getContext());
                GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                mViewModel.getNewEvents(geoPoint, progress).observe(getViewLifecycleOwner(), events -> {
                    mArrayAdapter.clear();
                    mArrayAdapter.addAll(events);
                    mNumberSwipe = 0;
                    TextView seekBarValue = rootView.findViewById(R.id.seekBar_value);
                    //seekBarValue.setText(progress +"km");
                });
            }



            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        mBinding.cardsListView.setOnItemClickListener((itemPosition, dataObject) -> {
            mInfoFragment = new EventDetailFragment(mEventList.get(0),this);
            getActivity().getSupportFragmentManager().beginTransaction().replace(this.getId(), mInfoFragment).commit();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}
