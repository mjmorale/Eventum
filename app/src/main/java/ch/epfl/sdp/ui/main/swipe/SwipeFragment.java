package ch.epfl.sdp.ui.main.swipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.R;
import ch.epfl.sdp.databinding.FragmentSwipeBinding;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.ui.main.FilterSettingsViewModel;

/**
 * Fragment for the swiping cards
 */
public class SwipeFragment extends Fragment implements SwipeFlingAdapterView.onFlingListener {

    private FragmentSwipeBinding mBinding;
    private ArrayAdapter<Event> mArrayAdapter;
    private List<Event> mEventList;
    private int mNumberSwipe = 0;

    private EventDetailFragment mInfoFragment;
    private FilterSettingsViewModel.FilterSettingsViewModelFactory mFactory;

    /**
     * Constructor of the swipe fragment, only for testing purpose!
     *
     * @param database {@link ch.epfl.sdp.db.Database}
     */
    @VisibleForTesting
    public SwipeFragment(@NonNull Database database) {
        mFactory = new FilterSettingsViewModel.FilterSettingsViewModelFactory();
        mFactory.setDatabase(database);
    }

    /**
     * Constructor of the swipe fragment
     */
    public SwipeFragment() {}

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
                new ViewModelProvider(requireActivity(), mFactory).get(FilterSettingsViewModel.class);

        filterSettingsViewModel.getFilteredEvents().observe(getViewLifecycleOwner(), events -> {
            mArrayAdapter.clear();
            mArrayAdapter.addAll(events);
            mNumberSwipe = 0;
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                showCardList();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        mBinding.cardsListView.setOnItemClickListener((itemPosition, dataObject) -> {
            mBinding.eventDetailView.setEvent(mEventList.get(itemPosition));
            showEventDetail();
        });

        return mBinding.getRoot();
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
}
