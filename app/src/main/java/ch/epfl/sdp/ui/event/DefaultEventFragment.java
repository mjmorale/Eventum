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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

import ch.epfl.sdp.databinding.FragmentDefaultEventBinding;

import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.platforms.google.map.GoogleMapManager;
import ch.epfl.sdp.ui.ServiceProvider;
import ch.epfl.sdp.ui.UIConstants;
import ch.epfl.sdp.ui.event.chat.ChatFragment;
import ch.epfl.sdp.ui.sharing.Sharing;
import ch.epfl.sdp.ui.sharing.SharingBuilder;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class DefaultEventFragment extends Fragment implements OnMapReadyCallback {

    private final DefaultEventViewModel.DefaultEventViewModelFactory mFactory;
    private DefaultEventViewModel mViewModel;

    private FragmentDefaultEventBinding mBinding;

    private Sharing mEventSharing;
    private float mZoomLevel = 15;

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
        mFactory.setDatabase(ServiceProvider.getInstance().getDatabase());
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

        return mBinding.getRoot();
   }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle args = getArguments();
        if(args != null) {
            mFactory.setEventRef(args.getString(UIConstants.BUNDLE_EVENT_REF));
        }

        mBinding.minimap.onCreate(savedInstanceState);
        mBinding.minimap.getMapAsync(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mBinding.minimap.onStart();
    }

    @Override
    public void onPause() {
        mBinding.minimap.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mBinding.minimap.onResume();
    }

    @Override
    public void onDestroy() {
        mBinding.minimap.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onStop() {
        mBinding.minimap.onStop();
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mBinding.minimap.onSaveInstanceState(outState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mFactory.setMapManager(new GoogleMapManager(googleMap));
        mViewModel = new ViewModelProvider(this, mFactory).get(DefaultEventViewModel.class);

        mEventSharing = new SharingBuilder().setRef(mViewModel.getEventRef()).build();
        mBinding.sharingButton.setOnClickListener(v->startActivity(mEventSharing.getShareIntent()));

        mViewModel.getEvent().observe(getViewLifecycleOwner(), event -> {
            mBinding.date.setText(event.getDateStr());
            mBinding.description.setText(event.getDescription());
            mBinding.title.setText(event.getTitle());
            mBinding.address.setText(event.getAddress());
            Glide.with(getContext()).load(event.getImageId()).into(mBinding.imageView);
            mViewModel.setEventOnMap(event.getLocation(), event.getTitle(), mZoomLevel);
        });

        mBinding.chatButton.setOnClickListener(v-> getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(getId(), ChatFragment.getInstance(mViewModel.getEventRef()))
                .addToBackStack(null)
                .commit());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
