package ch.epfl.sdp.ui.main.attending;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import ch.epfl.sdp.databinding.FragmentAttendingListBinding;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.platforms.firebase.db.FirestoreDatabase;

public class AttendingListFragment extends Fragment {

    private final AttendingListViewModel.AttendingListViewModelFactory mFactory;
    private AttendingListViewModel mViewModel;
    private FragmentAttendingListBinding mBinding;

    private AttendingEventAdapter mAdapter;

    public AttendingListFragment() {
        mFactory = new AttendingListViewModel.AttendingListViewModelFactory();
        mFactory.setDatabase(new FirestoreDatabase(FirebaseFirestore.getInstance()));
    }

    @VisibleForTesting
    public AttendingListFragment(@NonNull Database database) {
        mFactory = new AttendingListViewModel.AttendingListViewModelFactory();
        mFactory.setDatabase(database);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentAttendingListBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new AttendingEventAdapter(new ArrayList<>());
        mBinding.attendingListView.setAdapter(mAdapter);
        mBinding.attendingListView.setLayoutManager(new LinearLayoutManager(getContext()));

        mViewModel = new ViewModelProvider(this, mFactory).get(AttendingListViewModel.class);

        if(mViewModel.getAttendingEvents().hasObservers()) {
            mViewModel.getAttendingEvents().removeObservers(getViewLifecycleOwner());
        }
        mViewModel.getAttendingEvents().observe(getViewLifecycleOwner(), events -> {
            mAdapter.setAttendingEvents(events);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}
