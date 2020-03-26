package ch.epfl.sdp.ui.main.attending;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import ch.epfl.sdp.databinding.AttendingListFragmentBinding;
import ch.epfl.sdp.ui.DatabaseViewModelFactory;
import ch.epfl.sdp.ui.FirestoreDatabaseViewModelFactory;

public class AttendingListFragment extends Fragment {

    private AttendingListViewModel mViewModel;
    private AttendingListFragmentBinding mBinding;

    private AttendingEventAdapter mAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = AttendingListFragmentBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new AttendingEventAdapter(new ArrayList<>());
        mBinding.attendingListView.setAdapter(mAdapter);
        mBinding.attendingListView.setLayoutManager(new LinearLayoutManager(getContext()));

        DatabaseViewModelFactory factory = FirestoreDatabaseViewModelFactory.getInstance();
        mViewModel = new ViewModelProvider(this, factory).get(AttendingListViewModel.class);

        mViewModel.getAttendingEvents().observe(getViewLifecycleOwner(), events -> {
            mAdapter.setAttendingEvents(events);
        });
    }

}
