package ch.epfl.sdp.ui.event;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

import ch.epfl.sdp.databinding.FragmentDefaultEventBinding;

import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.platforms.firebase.db.FirestoreDatabase;
import ch.epfl.sdp.ui.UIConstants;
import ch.epfl.sdp.ui.main.swipe.SwipeFragment;
import ch.epfl.sdp.ui.sharing.Sharing;
import ch.epfl.sdp.ui.sharing.SharingBuilder;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class DefaultEventFragment extends Fragment{

    private DefaultEventViewModel mViewModel;
    private FragmentDefaultEventBinding mBinding;
    private final DefaultEventViewModel.DefaultEventViewModelFactory mFactory;
    private Sharing mEventSharing;

    public static DefaultEventFragment getInstance(@NonNull String eventRef) {
        verifyNotNull(eventRef);

        Bundle bundle = new Bundle();
        bundle.putString(UIConstants.BUNDLE_EVENT_REF, eventRef);

        DefaultEventFragment fragment = new DefaultEventFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public DefaultEventFragment() {
        mFactory = new DefaultEventViewModel.DefaultEventViewModelFactory();
        mFactory.setDatabase(new FirestoreDatabase(FirebaseFirestore.getInstance()));
    }

    @VisibleForTesting
    public DefaultEventFragment(@NonNull Database database, @NonNull String eventRef) {
        mFactory = new DefaultEventViewModel.DefaultEventViewModelFactory();
        mFactory.setDatabase(database);
        mFactory.setEventRef(eventRef);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentDefaultEventBinding.inflate(inflater, container, false);

        mBinding.backButton.setOnClickListener(v -> getActivity().getSupportFragmentManager().beginTransaction().replace(this.getId(), new SwipeFragment()).commit());

        return mBinding.getRoot();
   }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle args = getArguments();
        if(args != null) {
            mFactory.setEventRef(args.getString(UIConstants.BUNDLE_EVENT_REF));
        }

        mViewModel = new ViewModelProvider(this, mFactory).get(DefaultEventViewModel.class);

        mEventSharing = new SharingBuilder().setRef(mViewModel.getEventRef()).build();
        mBinding.sharingButton.setOnClickListener(v->startActivity(mEventSharing.getShareIntent()));

        mViewModel.getEvent().observe(getViewLifecycleOwner(), event -> {
            mBinding.date.setText(event.getDateStr());
            mBinding.description.setText(event.getDescription());
            mBinding.title.setText(event.getTitle());
            mBinding.address.setText(event.getAddress());
            if (event.getImageId() != null) Glide.with(getContext()).load(event.getImageId()).into(mBinding.imageView);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

}
