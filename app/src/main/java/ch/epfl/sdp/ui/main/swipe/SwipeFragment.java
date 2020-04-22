package ch.epfl.sdp.ui.main.swipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.R;
import ch.epfl.sdp.databinding.FragmentSwipeBinding;
import ch.epfl.sdp.ui.main.FilterSettingsViewModel;

public class SwipeFragment extends Fragment implements SwipeFlingAdapterView.onFlingListener {

    private FragmentSwipeBinding mBinding;
    private ArrayAdapter<Event> mArrayAdapter;
    private List<Event> mEventList;
    private int mNumberSwipe = 0;

    private EventDetailFragment mInfoFragment;

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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentSwipeBinding.inflate(inflater, container, false);

        mEventList = new ArrayList<>();
        mArrayAdapter = new CardArrayAdapter(getContext(), mEventList);
        mBinding.cardsListView.setAdapter(mArrayAdapter);
        mBinding.cardsListView.setFlingListener(this);

        FilterSettingsViewModel filterSettingsViewModel =
                new ViewModelProvider(requireActivity()).get(FilterSettingsViewModel.class);

        filterSettingsViewModel.getFilteredEvents().observe(getViewLifecycleOwner(), events -> {
            mArrayAdapter.clear();
            mArrayAdapter.addAll(events);
            mNumberSwipe = 0;
        });

        mBinding.cardsListView.setOnItemClickListener((itemPosition, dataObject) -> {
            mInfoFragment = new EventDetailFragment(mEventList.get(0),this);
            getActivity().getSupportFragmentManager().beginTransaction().replace(this.getId(), mInfoFragment).commit();
        });

        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}
