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

import java.lang.reflect.Parameter;
import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import ch.epfl.sdp.databinding.AttendingListFragmentBinding;
import ch.epfl.sdp.firebase.db.FirestoreDatabase;
import ch.epfl.sdp.ui.DatabaseViewModelFactory;
import ch.epfl.sdp.ui.FirestoreDatabaseViewModelFactory;
import ch.epfl.sdp.ui.ParameterizedViewModelFactory;

public class AttendingListFragment extends Fragment {

    private ParameterizedViewModelFactory mFactory;
    private AttendingListViewModel mViewModel;
    private AttendingListFragmentBinding mBinding;

    private AttendingEventAdapter mAdapter;

    public AttendingListFragment() {}

    public AttendingListFragment(ParameterizedViewModelFactory factory) {
        mFactory = factory;
    }

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

        if(mFactory == null) {
            mFactory = new ParameterizedViewModelFactory(new FirestoreDatabase(FirebaseFirestore.getInstance()));
        }
        mViewModel = new ViewModelProvider(this, mFactory).get(AttendingListViewModel.class);

        if(mViewModel.getAttendingEvents().hasObservers()) {
            mViewModel.getAttendingEvents().removeObservers(getViewLifecycleOwner());
        }
        mViewModel.getAttendingEvents().observe(getViewLifecycleOwner(), events -> {
            mAdapter.setAttendingEvents(events);
        });
    }
}
