package ch.epfl.sdp.ui.event.attendee;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.databinding.FragmentAttendeeListBinding;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.ui.ServiceProvider;
import ch.epfl.sdp.ui.UIConstants;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class AttendeeFragment extends Fragment {

    private AttendeeViewModel mViewModel;
    private final AttendeeViewModel.AttendeeViewModelFactory mFactory;
    private FragmentAttendeeListBinding mBinding;
    private AttendeeListAdapter mUserListAdapter;

    public static AttendeeFragment getInstance(@NonNull String eventRef) {
        Bundle bundle = new Bundle();
        bundle.putString(UIConstants.BUNDLE_EVENT_REF, verifyNotNull(eventRef));

        AttendeeFragment fragment = new AttendeeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public AttendeeFragment() {
        mFactory = new AttendeeViewModel.AttendeeViewModelFactory();
        mFactory.setDatabase(ServiceProvider.getInstance().getDatabase());
        mFactory.setAuthenticator(ServiceProvider.getInstance().getAuthenticator());
    }

    @VisibleForTesting
    public AttendeeFragment(@NonNull Database database, @NonNull String eventRef, @NonNull Authenticator authenticator) {
        mFactory = new AttendeeViewModel.AttendeeViewModelFactory();
        mFactory.setDatabase(database);
        mFactory.setAuthenticator(authenticator);
        mFactory.setEventRef(eventRef);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentAttendeeListBinding.inflate(inflater, container, false);

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle args = getArguments();
        if(args != null) {
            mFactory.setEventRef(verifyNotNull(args.getString(UIConstants.BUNDLE_EVENT_REF)));
        }

        mViewModel = new ViewModelProvider(this, mFactory).get(AttendeeViewModel.class);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setSmoothScrollbarEnabled(true);

        mUserListAdapter = new AttendeeListAdapter(mViewModel.getUserRef());
        mBinding.attendeeListview.setLayoutManager(layoutManager);
        mBinding.attendeeListview.setAdapter(mUserListAdapter);

        mViewModel.getAttendee().observe(getViewLifecycleOwner(), users -> {
            mUserListAdapter.clear();
            mUserListAdapter.addAll(users);
        });
    }
}
