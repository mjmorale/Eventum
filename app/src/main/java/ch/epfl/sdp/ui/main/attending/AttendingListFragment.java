package ch.epfl.sdp.ui.main.attending;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;

import androidx.recyclerview.widget.LinearLayoutManager;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.databinding.FragmentAttendingListBinding;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.offline.EventSaver;
import ch.epfl.sdp.ui.EventListAdapter;
import ch.epfl.sdp.ui.ServiceProvider;
import ch.epfl.sdp.ui.event.EventActivity;

/**
 * Fragment for the list of events a user attends to
 */
public class AttendingListFragment extends Fragment {

    private final AttendingListViewModel.AttendingListViewModelFactory mFactory;
    private AttendingListViewModel mViewModel;
    private FragmentAttendingListBinding mBinding;

    private EventListAdapter mAdapter;

    /**
     * Constructor for the AttendingListFragment
     */
    public AttendingListFragment() {
        mFactory = new AttendingListViewModel.AttendingListViewModelFactory();
        mFactory.setAuthenticator(ServiceProvider.getInstance().getAuthenticator());
        mFactory.setDatabase(ServiceProvider.getInstance().getDatabase());
        mFactory.setObjectSaver(new EventSaver());
    }

    /**
     * Constructor for the AttendingListFragment, only for testing purposes!
     *
     * @param database The database service to use
     * @param authenticator The authentication service to use
     */
    @VisibleForTesting
    public AttendingListFragment(@NonNull Authenticator authenticator, @NonNull EventSaver eventSaver, @NonNull File cacheDir, @NonNull Database database) {
        mFactory = new AttendingListViewModel.AttendingListViewModelFactory();
        mFactory.setAuthenticator(authenticator);
        mFactory.setObjectSaver(eventSaver);
        mFactory.setCacheDir(cacheDir);
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

        mAdapter = new EventListAdapter();
        mAdapter.setOnItemClickListener(event -> {
            Intent intent = EventActivity.getStartIntent(getContext(), EventActivity.EventActivityMode.ATTENDEE, event.getId());
            startActivity(intent);
        });
        mBinding.attendingListView.setAdapter(mAdapter);
        mBinding.attendingListView.setLayoutManager(new LinearLayoutManager(getContext()));

        // If injected using the constructor then do not get it from the context
        if(mFactory.getCacheDir() == null) {
            mFactory.setCacheDir(getContext().getFilesDir());
        }
        mViewModel = new ViewModelProvider(this, mFactory).get(AttendingListViewModel.class);

        mViewModel.getAttendingEvents().observe(getViewLifecycleOwner(), events -> {
            if(events != null) {
                mBinding.attendingEmptyListMsg.setVisibility(events.isEmpty() ? View.VISIBLE : View.GONE);
                mAdapter.clear();
                mAdapter.addAll(events);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}