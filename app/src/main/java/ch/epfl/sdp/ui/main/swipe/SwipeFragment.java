package ch.epfl.sdp.ui.main.swipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

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
import ch.epfl.sdp.db.DatabaseObject;
import ch.epfl.sdp.db.queries.Query;
import ch.epfl.sdp.db.queries.QueryResult;
import ch.epfl.sdp.ui.main.FilterSettingsViewModel;

public class SwipeFragment extends Fragment implements SwipeFlingAdapterView.onFlingListener {

    private FragmentSwipeBinding mBinding;
    private ArrayAdapter<DatabaseObject<Event>> mArrayAdapter;
    private int mNumberSwipe = 0;

    private EventDetailFragment mInfoFragment;
    private FilterSettingsViewModel.FilterSettingsViewModelFactory mFactory;
    private FilterSettingsViewModel mViewModel;

    @VisibleForTesting
    public SwipeFragment(@NonNull Database database) {
        mFactory = new FilterSettingsViewModel.FilterSettingsViewModelFactory();
        mFactory.setDatabase(database);
    }

    public SwipeFragment() {}

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
        mViewModel.joinEvent(((DatabaseObject<Event>) o).getId(), result -> {
            if(result.isSuccessful()) {
                Toast.makeText(getContext(), "success", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
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

        mViewModel = new ViewModelProvider(requireActivity(), mFactory).get(FilterSettingsViewModel.class);

        mViewModel.getFilteredEvents().observe(getViewLifecycleOwner(), events -> {
            mArrayAdapter.clear();
            mArrayAdapter.addAll(events);
            mArrayAdapter.sort((o1, o2) -> o1.getObject().getDate().compareTo(o2.getObject().getDate()));
            mNumberSwipe = 0;
        });

        mBinding.cardsListView.setOnItemClickListener((itemPosition, dataObject) -> {
            mInfoFragment = new EventDetailFragment(((DatabaseObject<Event>)dataObject).getObject(),this);
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
