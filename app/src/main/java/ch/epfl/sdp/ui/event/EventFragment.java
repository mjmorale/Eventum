package ch.epfl.sdp.ui.event;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import ch.epfl.sdp.databinding.EventDetailBinding;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.platforms.firebase.storage.ImageGetter;
import ch.epfl.sdp.platforms.google.map.GoogleMapManager;
import ch.epfl.sdp.ui.ServiceProvider;
import ch.epfl.sdp.ui.UIConstants;
import ch.epfl.sdp.ui.event.chat.ChatFragment;
import ch.epfl.sdp.ui.sharing.Sharing;
import ch.epfl.sdp.ui.sharing.SharingBuilder;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

/**
 * Fragment to display some detail information of an event
 */
public class EventFragment extends Fragment implements OnMapReadyCallback {

    private final DefaultEventViewModel.DefaultEventViewModelFactory mFactory;
    private DefaultEventViewModel mViewModel;

    private final LiteMapViewModel.LiteMapViewModelFactory mMapFactory;
    private LiteMapViewModel mMapViewModel;

    private EventDetailBinding mBinding;

    private Sharing mEventSharing;
    private float mZoomLevel = 15;
    private int LAUNCH_CALENDAR = 3;
    private final int  ONE_MINUTE = 60000;
    private final int ONE_HOUR = 60 * ONE_MINUTE;
    private final int THREE_HOURS = 3 * ONE_HOUR;

    /**
     * Method to create an instance of the fragment for a specific event
     *
     * @param eventRef the event reference
     * @return the fragment
     */
    public static EventFragment getInstance(@NonNull String eventRef) {
        verifyNotNull(eventRef);

        Bundle bundle = new Bundle();
        bundle.putString(UIConstants.BUNDLE_EVENT_REF, eventRef);

        EventFragment fragment = new EventFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * Constructor of the DefaultEventFragment
     */
    public EventFragment() {
        mFactory = new DefaultEventViewModel.DefaultEventViewModelFactory();
        mFactory.setDatabase(ServiceProvider.getInstance().getDatabase());

        mMapFactory = new LiteMapViewModel.LiteMapViewModelFactory();
    }

    /**
     * Constructor of the DefaultEventFragment, only for testing purposes!
     *
     * @param database
     * @param eventRef the reference of an event
     */
    @VisibleForTesting
    public EventFragment(@NonNull Database database, @NonNull String eventRef) {
        mFactory = new DefaultEventViewModel.DefaultEventViewModelFactory();
        mFactory.setDatabase(database);
        mFactory.setEventRef(eventRef);

        mMapFactory = new LiteMapViewModel.LiteMapViewModelFactory();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = EventDetailBinding.inflate(inflater, container, false);

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

        mViewModel = new ViewModelProvider(this, mFactory).get(DefaultEventViewModel.class);

        mViewModel.getEvent().observe(getViewLifecycleOwner(), event -> {
            mBinding.date.setText(event.getDateStr());
            mBinding.description.setText(event.getDescription());
            mBinding.title.setText(event.getTitle());
            mBinding.address.setText(event.getAddress());
            ImageGetter.getInstance().getImage(getContext(), event.getImageId(), mBinding.imageView);
        });

        mEventSharing = new SharingBuilder().setRef(mViewModel.getEventRef()).build();
        mBinding.eventDetailSharingButton.setOnClickListener(v->startActivity(mEventSharing.getShareIntent()));

        mBinding.eventDetailChatButton.setOnClickListener(v-> getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(getId(), ChatFragment.getInstance(mViewModel.getEventRef()))
                .addToBackStack(null)
                .commit());

        mBinding.eventDetailCalendarButton.setOnClickListener(v-> startActivityForResult(getCalendarIntent(), LAUNCH_CALENDAR));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMapFactory.setMapManager(new GoogleMapManager(googleMap));

        mMapViewModel = new ViewModelProvider(this, mMapFactory).get(LiteMapViewModel.class);

        mViewModel.getEvent().observe(getViewLifecycleOwner(), event -> {
            mMapViewModel.setEventOnMap(event.getLocation(), event.getTitle(), mZoomLevel);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_CALENDAR) {
            if(resultCode != Activity.RESULT_OK){
                Toast.makeText(getContext(), "No Calendar app found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    private Intent getCalendarIntent() {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType("vnd.android.cursor.item/event");
        mViewModel.getEvent().observe(getViewLifecycleOwner(), event -> {
            intent.putExtra(CalendarContract.Events.TITLE, event.getTitle());
            intent.putExtra(CalendarContract.Events.EVENT_LOCATION, event.getAddress());
            intent.putExtra(CalendarContract.Events.DESCRIPTION, event.getDescription());
            intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, event.getDate().getTime());
            intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, event.getDate().getTime()+THREE_HOURS);
        });

        return intent;
    }
}
