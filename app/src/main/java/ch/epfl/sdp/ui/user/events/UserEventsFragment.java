package ch.epfl.sdp.ui.user.events;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.databinding.FragmentUserEventsBinding;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.ui.EventListAdapter;
import ch.epfl.sdp.ui.ServiceProvider;
import ch.epfl.sdp.ui.UIConstants;
import ch.epfl.sdp.ui.event.EventActivity;

/**
 * Fragment to display the user's events
 */
public class UserEventsFragment extends Fragment {

    private FragmentUserEventsBinding mBinding;
    private UserEventsViewModel mViewModel;
    private final UserEventsViewModel.UserEventsViewModelFactory mFactory;

    private EventListAdapter mAdapter;

    /**
     * Constructor of the UserEventsFragment
     */
    public UserEventsFragment() {
        mFactory = new UserEventsViewModel.UserEventsViewModelFactory();
        mFactory.setDatabase(ServiceProvider.getInstance().getDatabase());
        mFactory.setAuthenticator(ServiceProvider.getInstance().getAuthenticator());
    }

    /**
     * Constructor of the UserEventsFragment, only for testing purpose!
     *
     * @param database {@link ch.epfl.sdp.db.Database}
     */
    @VisibleForTesting
    public UserEventsFragment(@NonNull Database database, @NonNull Authenticator authenticator) {
        mFactory = new UserEventsViewModel.UserEventsViewModelFactory();
        mFactory.setDatabase(database);
        mFactory.setAuthenticator(authenticator);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentUserEventsBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new EventListAdapter();
        mBinding.userEventsListview.setAdapter(mAdapter);
        mBinding.userEventsListview.setLayoutManager(new LinearLayoutManager(getContext()));

        mViewModel = new ViewModelProvider(this, mFactory).get(UserEventsViewModel.class);

        mViewModel.getOrganizedEvents().observe(getViewLifecycleOwner(), events -> {
            mBinding.userEventsEmptyMsg.setVisibility(events.isEmpty() ? View.VISIBLE : View.GONE);
            mAdapter.clear();
            mAdapter.addAll(events);
        });

        mAdapter.setOnItemClickListener(event -> {
            Intent intent = new Intent(getContext(), EventActivity.class);
            intent.putExtra(UIConstants.BUNDLE_EVENT_MODE_REF, EventActivity.EventActivityMode.ORGANIZER);
            intent.putExtra(UIConstants.BUNDLE_EVENT_REF, event.getId());
            startActivity(intent);
        });
    }
}
