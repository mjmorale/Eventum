package ch.epfl.sdp.ui.main.attending;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import ch.epfl.sdp.databinding.FragmentAttendingListBinding;
import ch.epfl.sdp.ui.main.FilterSettingsViewModel;

public class AttendingListFragment extends Fragment {

    private FragmentAttendingListBinding mBinding;

    private AttendingEventAdapter mAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentAttendingListBinding.inflate(inflater, container, false);

        mAdapter = new AttendingEventAdapter(new ArrayList<>());
        mBinding.attendingListView.setAdapter(mAdapter);
        mBinding.attendingListView.setLayoutManager(new LinearLayoutManager(getContext()));

        FilterSettingsViewModel filterSettingsViewModel =
                new ViewModelProvider(requireActivity()).get(FilterSettingsViewModel.class);

        filterSettingsViewModel.getFilteredEvents().observe(getViewLifecycleOwner(), events -> {
            mAdapter.setAttendingEvents(new ArrayList<>(events));
        });

        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}
